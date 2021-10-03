package helpers

import org.ergoplatform.ErgoAddressEncoder
import org.ergoplatform.appkit.{Address, ErgoClient, NetworkType, RestApiErgoClient}
import java.math.BigInteger

object Configs extends ConfigHelper {
  lazy val nodeUrl: String = readKey("node.url")
  lazy val nodeApiKey: String = readKey("node.apiKey", "")
  lazy val networkType: NetworkType = if (readKey("node.networkType").toLowerCase.equals("mainnet")) NetworkType.MAINNET else NetworkType.TESTNET
  lazy val addressEncoder = new ErgoAddressEncoder(networkType.networkPrefix)
  private lazy val explorerUrlConf = readKey("explorer.url", "")
  lazy val explorerUrl: String = if (explorerUrlConf.isEmpty) RestApiErgoClient.getDefaultExplorerUrl(Configs.networkType) else explorerUrlConf
  lazy val defaultTxFee: Long = readKey("default.fee").toLong

  lazy val receiverSecret: BigInteger = BigInt(readKey("receiver.secret"), 16).bigInteger
  lazy val receiverAddress: String = readKey("receiver.address")
  lazy val amount: Long = readKey("amount").toLong

    lazy val susyTokens: Map[String, String]= getTokens("proxy.url")
    lazy val susyContracts: Map[String, String]= getContracts("proxy.url")

    lazy val tokenId: String = susyTokens("tokenId")
    lazy val maintainerTokenId: String = susyTokens("maintainerTokenId")
    lazy val linkListTokenId: String = susyTokens("linkListTokenId")
    lazy val linkListRepoTokenId: String = susyTokens("linkListRepoTokenId")
    lazy val tokenRepoTokenId: String = susyTokens("tokenRepoTokenId")

    lazy val linkListAddress: String = susyContracts("linkListAddress")
    lazy val maintainerAddress: String = susyContracts("maintainerAddress")
    lazy val linkListElementAddress: String = susyContracts("linkListElementAddress")
    lazy val signalAddress: String = susyContracts("signalAddress")
    lazy val tokenRepoAddress: String = susyContracts("tokenRepoAddress")

//  lazy val tokenId: String = readKey("tokenId")
//  lazy val maintainerTokenId: String = readKey("maintainerTokenId")
//  lazy val linkListTokenId: String = readKey("linkListTokenId")
//  lazy val linkListRepoTokenId: String = readKey("linkListRepoTokenId")
//  lazy val tokenRepoTokenId: String = readKey("tokenRepoTokenId")
//
//  lazy val linkListAddress: String = readKey("linkListAddress")
//  lazy val maintainerAddress: String = readKey("maintainerAddress")
//  lazy val linkListElementAddress: String = readKey("linkListElementAddress")
//  lazy val signalAddress: String = readKey("signalAddress")
//  lazy val tokenRepoAddress: String = readKey("tokenRepoAddress")

}
