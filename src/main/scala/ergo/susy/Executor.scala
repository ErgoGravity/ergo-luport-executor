package ergo.susy

import org.ergoplatform.appkit._
import java.io.PrintWriter
import org.ergoplatform.appkit.impl.ErgoTreeContract
import org.ergoplatform.appkit.{Address, ErgoToken, InputBox, OutBox}

import scala.collection.JavaConverters._
import java.security.SecureRandom

object Executor {

  private def selectRandomBox(seq: Seq[InputBox]): Option[InputBox] = {
    val random = new SecureRandom()
    new scala.util.Random(random).shuffle(seq).headOption
  }

  private def getSpecBox(ctx: BlockchainContext, settings: Setting, typeBox: String, random: Boolean = false): InputBox = {
    val boxData = typeBox match {
      case "linkList" =>
        ("linkList", settings.linkListAddress, settings.linkListTokenId)
      case "maintainer" =>
        ("maintainer", settings.maintainerAddress, settings.maintainerTokenId)
      case "linkListElement" =>
        ("linkListElement", settings.linkListElementAddress, settings.linkListRepoTokenId)
      case "tokenId" =>
        ("tokenId", settings.receiverAddress, settings.tokenId)
      case "fee" =>
        ("fee", settings.receiverAddress, "")
    }
    val boxes = ctx.getCoveringBoxesFor(Address.create(boxData._2), (1e9 * 1e8).toLong).getBoxes.asScala.toList
    val box = if (boxData._1.equals("fee"))
      boxes.filter(box => box.getValue >= settings.defaultTxFee * 2 && box.getTokens.size == 0)
    else if (boxData._1.equals("maintainer"))
      boxes.filter(box => box.getTokens.size() > 0 &&
        box.getTokens.get(0).getId.toString.equals(boxData._3) &&
        box.getTokens.get(1).getId.toString.equals(settings.tokenId)
      )
    else
      boxes.filter(box => box.getTokens.size() > 0 && box.getTokens.get(0).getId.toString.equals(boxData._3))

    if (random) selectRandomBox(box).orNull else box.headOption.orNull
  }

  def createWrapRequest(ctx: BlockchainContext, settings: Setting, prover: ErgoProver, linkListTokenRepoBox: InputBox, maintainerBox: InputBox, tokenIdBox: InputBox, feeBox: InputBox): Unit = {
    val txB = ctx.newTxBuilder()

    def createOutputBoxes(txB: UnsignedTransactionBuilder, linkListTokenRepoBox: InputBox, maintainerBox: InputBox): (OutBox, OutBox, OutBox, OutBox) = {
      val lastRequestId = BigInt(JavaHelpers.SigmaDsl.toBigInteger(linkListTokenRepoBox.getRegisters.get(0).getValue.asInstanceOf[special.sigma.BigInt]))
      val nftNumber = linkListTokenRepoBox.getRegisters.get(2).getValue.asInstanceOf[Int]
      val newRequestId = (lastRequestId + BigInt(nftNumber + 1)).bigInteger

      val linklist = txB.outBoxBuilder
        .value(linkListTokenRepoBox.getValue - settings.defaultTxFee)
        .tokens(linkListTokenRepoBox.getTokens.get(0),
          new ErgoToken(linkListTokenRepoBox.getTokens.get(1).getId, linkListTokenRepoBox.getTokens.get(1).getValue - 1))
        .registers(
          ErgoValue.of(newRequestId),
          linkListTokenRepoBox.getRegisters.get(1),
          linkListTokenRepoBox.getRegisters.get(2))
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
      if (maintainerBox.getTokens.size > 1) {
        requestValue = maintainerBox.getValue
        tokenAmount = amount
      } else {
        requestValue = maintainerBox.getValue + amount
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
        .contract(new ErgoTreeContract(Address.create(settings.receiverAddress).getErgoAddress.script))
        .build()
      (linklist, linkListElement, maintainer, tokenId)
    }

    val (linklistOut, linkListElementOut, maintainerOut, tokenIdOut) = createOutputBoxes(txB, linkListTokenRepoBox, maintainerBox)

    val tx = txB.boxesToSpend(Seq(linkListTokenRepoBox, maintainerBox, tokenIdBox, feeBox).asJava)
      .outputs(linklistOut, linkListElementOut, maintainerOut, tokenIdOut)
      .fee(settings.defaultTxFee)
      .sendChangeTo(Address.create(settings.receiverAddress).getErgoAddress)
      .build()

    val signed: SignedTransaction = prover.sign(tx)
    new PrintWriter(s"result/luportWrapRequest.txt") {
      write(signed.toJson(false))
      close()
    }
    val txId = ctx.sendTransaction(signed)
    new PrintWriter(s"result/luportWrapRequest_txId.txt") {
      write(txId)
      close()
    }
    println(s"txId: $txId")
  }

  def run(ctx: BlockchainContext, settings: Setting): Unit = {

      val prover = ctx.newProverBuilder()
        .withDLogSecret(settings.receiverSecret)
        .build()

      val linkListTokenRepoBox = getSpecBox(ctx, settings,"linkList")
      val maintainerBox = getSpecBox(ctx, settings, "maintainer")
      val tokenIdBox = getSpecBox(ctx, settings, "tokenId")
      val feeBox = getSpecBox(ctx, settings, "fee", random = true)
      println(linkListTokenRepoBox.getId.toString)
      println(maintainerBox.getId.toString)
      println(tokenIdBox.getId.toString)
      println(feeBox.getId.toString)

      createWrapRequest(ctx, settings, prover, linkListTokenRepoBox, maintainerBox, tokenIdBox, feeBox)
  }
}
