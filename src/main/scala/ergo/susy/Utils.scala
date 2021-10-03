package ergo.susy

import helpers.Configs
import helpers.Configs.{explorerUrl, networkType, nodeApiKey, nodeUrl}
import org.ergoplatform.appkit.{BlockchainContext, ErgoClient, RestApiErgoClient}

object Utils {
  val ergoClient: ErgoClient = RestApiErgoClient.create(nodeUrl, networkType, nodeApiKey, explorerUrl)
  def sendTx(settings: Setting): Unit = {
    ergoClient.execute((ctx: BlockchainContext) => {
      println(s"mode: $settings.mode.toLowerCase")
      settings.mode.toLowerCase match {
        case "d" => Executor.run(ctx, settings)
        case "c" => {
          val newConfig = Setting.apply(
            explorerUrl = Configs.explorerUrl,
            nodeUrl = Configs.nodeUrl,
            nodeApiKey = Configs.nodeApiKey,
            nodeNetworkType = Configs.networkType,

            receiverSecret = Configs.receiverSecret,
            receiverAddress = Configs.receiverAddress,
            defaultTxFee = Configs.defaultTxFee,
            amount = Configs.amount,

            tokenId = Configs.tokenId,
            maintainerTokenId = Configs.maintainerTokenId,
            linkListTokenId = Configs.linkListTokenId,
            linkListRepoTokenId = Configs.linkListRepoTokenId,
            tokenRepoTokenId = Configs.tokenRepoTokenId,

            maintainerAddress = Configs.maintainerAddress,
            linkListAddress = Configs.linkListAddress,
            linkListElementAddress = Configs.linkListElementAddress,
            tokenRepoAddress = Configs.tokenRepoAddress,
            signalAddress = Configs.signalAddress,
            mode = "c")
          Executor.run(ctx, newConfig)
        }
      }
    })
  }
}
