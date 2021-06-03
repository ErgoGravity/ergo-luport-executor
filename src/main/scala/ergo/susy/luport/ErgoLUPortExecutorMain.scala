package ergo.susy.luport

import scopt.OptionParser

case class Config(
    nodeUrl: String = "http://213.239.193.208:9053",
    senderPrivKeyStr: String = "",
    tokenAddress: String = "",
    ibportAddress: String = "",
    receiverAddress: String = "",
    amount: Long = 0L)

object ErgoLUPortExecutor extends App {
  val parser: OptionParser[Config] = new OptionParser[Config]("ErgoSusyLuPort") {
//    override def showUsageOnError = Option(true)
    opt[Long]("amount")
      .action((x, c) => c.copy(amount = x))
      .text("amount is a Long value")
      .required()

    opt[String]("nodeUrl")
      .action((x, c) => c.copy(nodeUrl = x))
      .text("nodeUrl is a string value ex: http://213.239.193.208:9053")
      .optional()

    opt[String]("senderPrivKeyStr")
      .action((x, c) => c.copy(senderPrivKeyStr = x))
      .text("senderPrivKeyStr is a string hex value")
      .required()

    opt[String]("tokenAddress")
      .action((x, c) => c.copy(tokenAddress = x))
      .text("tokenAddress is a string hex value")
      .required()

    opt[String]("ibportAddress")
      .action((x, c) => c.copy(ibportAddress = x))
      .text("ibportAddress is a string base58 value")
      .required()

    opt[String]("receiverAddress")
      .action((x, c) => c.copy(receiverAddress = x))
      .text("receiverAddress is a string base58 value")
      .required()

    version('v', "version").text("version of ErgoSusyLuPort")
    help("help").text("prints this usage text")
  }

  parser.parse(args, Config()) match {
    case Some(config) =>
        Utils.sendTx(config)
    case None =>
    // arguments are bad, error message will have been displayed
  }
}
