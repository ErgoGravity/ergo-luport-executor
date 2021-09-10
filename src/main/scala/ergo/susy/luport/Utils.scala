package ergo.susy.luport

import helpers.Configs
import helpers.Configs.{explorerUrl, networkType, nodeApiKey, nodeUrl}
import org.ergoplatform.appkit.{BlockchainContext, ErgoClient, RestApiErgoClient}

object Utils {
  val ergoClient: ErgoClient = RestApiErgoClient.create(nodeUrl, networkType, nodeApiKey, explorerUrl)
  println(Configs.nodeUrl)

  def sendTx(settings: Setting): Unit = {
    ergoClient.execute((ctx: BlockchainContext) => {
      println(s"mode: $settings.mode.toLowerCase")
      settings.mode.toLowerCase match {
        case "d" => Executor.run(ctx, settings)
        case "c" => {
          val newConfig = Setting.apply(explorerUrl = Configs.explorerUrl,
            nodeUrl = Configs.nodeUrl,
            nodeApiKey = Configs.nodeApiKey,
            nodeNetworkType = Configs.networkType,
            senderPrivKeyStr = Configs.senderPrivKeyStr,
            tokenId = Configs.tokenId,
            receiverAddress = Configs.receiverAddress,
            defaultTxFee = Configs.defaultTxFee,
            amount = Configs.amount,
            feeAddress = Configs.feeAddress,
            maintainerTokenId = Configs.maintainerTokenId ,
            linklistTokenId = Configs.linklistTokenId,
            linklistRepoTokenId = Configs.linklistRepoTokenId,
            maintainerAddress = Configs.maintainerAddress,
            linkListAddress = Configs.linkListAddress,
            linkListElementAddress = Configs.linkListElementAddress,
            mode = "c")
          Executor.run(ctx, newConfig)
        }
      }
    })
//    println(configs)
  }
}
