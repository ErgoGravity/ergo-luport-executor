package ergo.susy

import org.ergoplatform.appkit.NetworkType
import org.ergoplatform.appkit.Address
import scopt.OptionParser

import java.math.BigInteger

case class Setting(
                    explorerUrl: String = "",
                    nodeUrl: String = "",
                    nodeApiKey: String = "",
                    nodeNetworkType: NetworkType = NetworkType.MAINNET,

                    receiverSecret: BigInteger = BigInt("64fe5c59d2a72c19ba4c3aabe649223c71529ebc2024bda96ceb33a22578108", 16).bigInteger,
                    receiverAddress: String = "",
                    tokenId: String = "",

                    defaultTxFee: Long = 1000000,
                    amount: Long = 1L,

                    maintainerTokenId: String = "",
                    linkListTokenId: String = "",
                    linkListRepoTokenId: String = "",
                    tokenRepoTokenId: String = "",

                    maintainerAddress: String = "",
                    linkListAddress: String = "",
                    linkListElementAddress: String = "",
                    tokenRepoAddress: String = "",
                    signalAddress: String = "",

                    mode: String = "d", // d for default and c for custom
                  )

object ErgoExecutor extends App {
  val parser: OptionParser[Setting] = new OptionParser[Setting]("ErgoSusyLuPort") {
    opt[String]('m', "mode")
      .action((x, c) => c.copy(mode = x))
      .text("d for default and c for custom")
      .required()
    help("help").text("prints this usage text")
  }

  parser.parse(args, Setting()) match {
    case Some(setting) =>
      Utils.sendTx(setting)
    case None =>
  }
}
