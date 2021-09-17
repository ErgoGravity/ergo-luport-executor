package ergo.susy.luport

import org.ergoplatform.appkit._
import java.io.PrintWriter
import org.ergoplatform.appkit.impl.ErgoTreeContract
import org.ergoplatform.appkit.{Address, ErgoToken, InputBox, OutBox}
import scala.collection.JavaConverters._

import sigmastate.eval._

object Executor {
  private def getSpecBox(ctx: BlockchainContext, typeBox: String, settings: Setting): InputBox = {
    val boxData = typeBox match {
      case "linkList" =>
        ("linkList", settings.linkListAddress, settings.linklistTokenId)
      case "maintainer" =>
        ("maintainer", settings.maintainerAddress, settings.maintainerTokenId)
      case "linkListElement" =>
        ("linkListElement", settings.linkListElementAddress, settings.linklistRepoTokenId)
      case "tokenId" =>
        ("tokenId", settings.feeAddress.toString, settings.tokenId)
      case "proxy" =>
        ("proxy", settings.feeAddress.toString, "")
    }

    val boxes = ctx.getCoveringBoxesFor(Address.create(boxData._2), (1e9 * 1e8).toLong).getBoxes.asScala.toList
    val box = if (boxData._1.equals("proxy"))
      boxes.filter(box => box.getValue >= settings.defaultTxFee * settings.amount && box.getTokens.size == 0)
    else if (boxData._1.equals("maintainer"))
      boxes.filter(box => box.getTokens.size() > 0 &&
        box.getTokens.get(0).getId.toString.equals(boxData._3) &&
        box.getTokens.get(1).getId.toString.equals(settings.tokenId)
      )
    else
      boxes.filter(box => box.getTokens.size() > 0 && box.getTokens.get(0).getId.toString.equals(boxData._3))

    box.headOption.orNull
  }

  def createLinkListElementBox(ctx: BlockchainContext, settings: Setting, prover: ErgoProver, boxFee: InputBox, linkListTokenRepoBox: InputBox, maintainerBox: InputBox, tokenIdBox: InputBox): Unit = {
    val txB = ctx.newTxBuilder()

    def createOutputBoxes(txB: UnsignedTransactionBuilder, linkListTokenRepoBox: InputBox, maintainerBox: InputBox): (OutBox, OutBox, OutBox, OutBox) = {
      val lastRequestId = BigInt(linkListTokenRepoBox.getRegisters.get(0).getValue.asInstanceOf[special.sigma.BigInt])
      val nftCount = linkListTokenRepoBox.getRegisters.get(1).getValue.asInstanceOf[Int]
      val nftNumber = linkListTokenRepoBox.getRegisters.get(2).getValue.asInstanceOf[Int]

      val linklist = txB.outBoxBuilder
        .value(linkListTokenRepoBox.getValue - settings.defaultTxFee)
        .tokens(new ErgoToken(linkListTokenRepoBox.getTokens.get(0).getId, 1),
          new ErgoToken(linkListTokenRepoBox.getTokens.get(1).getId, linkListTokenRepoBox.getTokens.get(1).getValue - 1))
        .registers(ErgoValue.of((lastRequestId + BigInt(nftNumber + 1)).bigInteger),
          ErgoValue.of(nftCount),
          ErgoValue.of(nftNumber)
        )
        .contract(new ErgoTreeContract(Address.create(settings.linkListAddress).getErgoAddress.script))
        .build()

      val receiver = settings.receiverAddress.getBytes()
      val linkListElement = txB.outBoxBuilder
        .value(settings.defaultTxFee)
        .tokens(new ErgoToken(linkListTokenRepoBox.getTokens.get(1).getId, 1))
        .registers(ErgoValue.of(receiver),
          ErgoValue.of(settings.amount),
          ErgoValue.of((lastRequestId + BigInt(nftNumber + 1)).bigInteger)
        )
        .contract(new ErgoTreeContract(Address.create(settings.linkListElementAddress).getErgoAddress.script))
        .build()

      val fee: Int = maintainerBox.getRegisters.get(0).getValue.asInstanceOf[Int]
      val amount: Long = settings.amount + fee * settings.amount / 10000
      var requestValue = 0L
      var tokenAmount = 0L
      if (settings.tokenId.isEmpty) {
        requestValue = maintainerBox.getValue + amount
      } else {
        requestValue = maintainerBox.getValue
        tokenAmount = amount
      }
      val maintainer = txB.outBoxBuilder
        .value(requestValue)
        .tokens(new ErgoToken(maintainerBox.getTokens.get(0).getId, 1),
          new ErgoToken(maintainerBox.getTokens.get(1).getId, maintainerBox.getTokens.get(1).getValue + tokenAmount))
        .registers(ErgoValue.of(fee))
        .contract(new ErgoTreeContract(Address.create(settings.maintainerAddress).getErgoAddress.script))
        .build()

      val tokenId = txB.outBoxBuilder
        .value(tokenIdBox.getValue)
        .tokens(new ErgoToken(tokenIdBox.getTokens.get(0).getId, tokenIdBox.getTokens.get(0).getValue - tokenAmount))
        .contract(new ErgoTreeContract(settings.feeAddress.getErgoAddress.script))
        .build()
      (linklist, linkListElement, maintainer, tokenId)
    }


    val (linklistOut, linkListElementOut, maintainerOut, tokenIdOut) = createOutputBoxes(txB, linkListTokenRepoBox, maintainerBox)

    val tx = txB.boxesToSpend(Seq(linkListTokenRepoBox, maintainerBox, tokenIdBox, boxFee).asJava)
      .outputs(linklistOut, linkListElementOut, maintainerOut, tokenIdOut)
      .fee(settings.defaultTxFee)
      .sendChangeTo(settings.feeAddress.getErgoAddress)
      .build()

    val signed: SignedTransaction = prover.sign(tx)
    new PrintWriter(s"result/LinkliestElementBox_signed.txt") {
      write(signed.toJson(false));
      close()
    }
    val txId = ctx.sendTransaction(signed)
    new PrintWriter(s"result/LinkliestElementBox_txId.txt") {
      write(txId);
      close()
    }
    println(s"txId: $txId")
  }

  def run(ctx: BlockchainContext, settings: Setting): Unit = {
    println(settings)

    val secret = BigInt(settings.senderPrivKeyStr, 16)
    val prover = ctx.newProverBuilder()
      .withDLogSecret(secret.bigInteger)
      .build()

    val linklistBox = getSpecBox(ctx, "linkList", settings)
    println(linklistBox)
    val maintainerBox = getSpecBox(ctx, "maintainer", settings)
    println(maintainerBox)
    val feeBox = getSpecBox(ctx, "proxy", settings)
    println(feeBox)
    val tokenIdBox = getSpecBox(ctx, "tokenId", settings)
    println(tokenIdBox)

    createLinkListElementBox(ctx, settings, prover, feeBox, linklistBox, maintainerBox, tokenIdBox)
  }
}
