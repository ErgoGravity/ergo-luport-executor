package helpers

import org.ergoplatform.ErgoAddressEncoder
import org.ergoplatform.appkit.{Address, ErgoClient, NetworkType, RestApiErgoClient}

object Configs extends ConfigHelper {
  lazy val nodeUrl: String = readKey("node.url")
  lazy val nodeApiKey: String = readKey("node.apiKey", "")
  lazy val networkType: NetworkType = if (readKey("node.networkType").toLowerCase.equals("mainnet")) NetworkType.MAINNET else NetworkType.TESTNET
  lazy val addressEncoder = new ErgoAddressEncoder(networkType.networkPrefix)
  private lazy val explorerUrlConf = readKey("explorer.url", "")
  lazy val explorerUrl: String = if (explorerUrlConf.isEmpty) RestApiErgoClient.getDefaultExplorerUrl(Configs.networkType) else explorerUrlConf
  lazy val defaultTxFee: Long = readKey("default.fee").toLong

  lazy val feeAddress: Address = Address.create(readKey("feeAddress"))

  lazy val senderPrivKeyStr: String = readKey("senderPrivKeyStr")
  lazy val receiverAddress: String = readKey("receiverAddress")
  lazy val amount:Long = readKey("amount").toLong

  lazy val tokenId: String = readKey("token.id ")

  lazy val maintainerTokenId: String = readKey("maintainerTokenId")
  lazy val linklistTokenId: String = readKey("linkListTokenId")
  lazy val linklistRepoTokenId: String = readKey("linkListRepoTokenId")

  lazy val linkListAddress: String = readKey("linkListAddress")
  lazy val maintainerAddress: String = readKey("maintainerAddress")
  lazy val linkListElementAddress: String = readKey("linkListElementAddress")

}
