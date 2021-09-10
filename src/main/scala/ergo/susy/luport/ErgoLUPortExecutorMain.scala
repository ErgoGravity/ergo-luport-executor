package ergo.susy.luport

import org.ergoplatform.appkit.NetworkType
import org.ergoplatform.appkit.Address
import scopt.OptionParser

case class Setting(
                    explorerUrl: String = "http://37.156.20.158:7000",
                    nodeUrl: String = "http://37.156.20.158:9064",
                    nodeApiKey: String = "hello",
                    nodeNetworkType: NetworkType = NetworkType.MAINNET,

                    feeAddress: Address = Address.create("9h6odKstXL1ExJTaPWdrk6d3CVhAJodeFwAnNWKQAVucywRyqrk"),
                    senderPrivKeyStr: String = "bae3ef240ca2e6398ca58446a4b42aa3c24cf90a841aad72e827fbc5b29390b5",
                    tokenId: String = "5237776f855127cc1e00f27815c7b37a83e7c2b500aea8dd9f45fce81daef0d7",
                    receiverAddress: String = "9gMbbSbYMn7KiDmvPSSHiBEu9CkjrJXb3TTDjm87NkqbVTj15NM",

                    defaultTxFee: Long = 1000000,
                    amount: Long = 1L,

                    maintainerTokenId: String = "207e24da3135f329636219eb9713c84068e2cc1df9a4b08d21a58fad1b54d9f6",
                    linklistTokenId: String = "23667c53bb2e6e55372cdb97bba4b8b03423c8368ce7f8915fa26e33c563dc76",
                    linklistRepoTokenId: String = "79650a9151ca7cc744ceae391a24c872d42b0cb51a6b7db9fbcb88a3b0d53bff",

                    maintainerAddress: String = "o9pjo7RUsohzxH85neDKFdRrHdmWWatoP6G3oQTHEHJDpHiapCRoTpiMMwthxKSZhR3JdKMQa4uMYrXVZRDZHEKPh7FK7u95AqP8kjRYpAadYq3XYuDCRUr5mbwvsYgNLRbeYJe4PPsrgr7vNSbgnHy5cGiztdeTZ6MLh16TdSCuhgZHJuvDvCHUr94RsnoZH6AYaPh64gZYR7Qi9iw5sQjzeymzet4aSL89MZpfWdSvYZm9hrAJ9prk74sq8E511UJdFRsRfvrCpUgXdResZcoMPwjbmf6GAB47knFyFSX2tCRWH3B1CmjsJ7v2WRzjznVxicaoRVxKRvCeNwt3EWJSgtXYhYQechW9Dj8k3cpuo7MEqE2LwQuWZxjLo2NhNJdDj3ArN1GE2RfW9obyG1eu2fACbiqeSzUksUracqypiqWsSxvZkJhrqADBrV2rfRnTnhvM9ZB9LXBRUd77SjBHukMcLHtiPKXgTuKUshyukRpPmuNK9GSSnYDaxMBxBjC43YEPqYhMNiVZjaL4hLpNh83Jju7d716X3iXRCUcwwtT7ZZHuDPo9GrYvyTmhm3whGyu4R3S1J4pYiYezaukVcUttDBEuYnFDQzBLG7k6PDcmwwUJx9gQoKJd4HwkJHZ5TLowNKPebzuqG1eeq6EFxEvjLQDfWBFVwWrKMM4vhF8s2nmbkeTMWRjNiKUGWNyQGhxqdzjZUYFHNsLQdXCstkhqcxTMaPFk6Jv2uXmPWuFXr7msthYyTC2fubHGTutmNgw6SBWGZE34SE61qDcEEGKS1Rg1b2t5CZepzjw7NBNakz3fQbKDRebSMC39HxqyhvfMEyNUBLxi93f5KzJsQDcas7ohk9tEyADQZmRHPmih7BW9eYBQ2UVLNGF3qbvEqCMeWyKz383QhVjvwK3N5JUNYpPNAxAUVc5f6L2T1tVqsoz7imQ7oUPwUzm4d2HP1oPtb5W8ERxMou1yVKEzUTXkxiC37YDbKGHGV9LQosSTyAdSVpXkYdqpua5wxtcjWrMFh6ZXFy4SGTiBb8TEFk3uJaYAAiyHbe7AuLUCv4Qa7sCaUaxPPQBtvL7x7MyRDoZBimzX1aCxCpLsTpMBJYTQbJoPRGin5hY",
                    linkListAddress: String = "3t29ZyMKgDirjqzDR4WkKpy4YUc25VhEPhWrTVBEtTifUYpe9b2JnF7maRV399uJdewEViw6jwfpry5QgwENfvYhdRMEQUNDt7gM2sxBfR6G3vf5ffqXp2KeQknCyXcZcpZhKqGBpJXX5ULACgcq97MLitYdhTswove2kSBvAqhf8dcRDfj6dye6MdRw2d7yizi9Yg3cK8BNwY1udKUgddCXdriwPw4DDJyidV6z4jstUcBVJEpxosRBNseUqp4UoWiLshShdg6gJS917n6rdhF2tPYGZYRWMrMEts8L5hQgkoqnoE5T3FTpZVhaEAbMDWAVaQwi6ogWL1EgRySoaH2Zgm7tAaSSUCDVuNdKAZA81DduuueSwYBPqHX3pQsXKRwbe1Res1H5ifDCMhSfHbYDcoDMVJGLtMzMX6JuKeiPRFgn4hN2shjVYkuPXd4zXUoh7sSt2sVXURSfacQEAhJkxeG1up8wTX5pRiKfJbGK3LFMsGPWZBdP99L79M4GrTFMFcnp28g9RQyHzaj6yb1YdY1e9zyyYAeKKQtMtFDtQyVJdFpKAEtVc54Vy9vipXbpJyq9LHdVUCfVvxW6wW22TjoJiZVGb1ZtgRxWpuP2XPw65zV2RYJ2gjVRb5rf977WvdZNw2oZBimkgjPJWTSiCauqFjRqZaBWV99juBpEeDmHW7ZiessQsinmEf9MmEPJAzhkqV6ezPWaMqbV2qz1yN1b7vSHCET2jWkGiNA1WiKsXf2KkWA2KKkgovNrPt8CZ2r4QZM9B3EZcJ1VC",
                    linkListElementAddress: String = "2HdmYx5JfmeXaUEaPNCLZbRmkGdocRBznJiTarsx9HnWjfSX8crSkFEGxYKxMPZ9tURve2hS2g4nBcgtEuJkx349BghFA8Lx64oZbe1BBm6MsRv8tVDdTyEfmZAZcevxzDEFy8vdLfRm2NTrx91FW1frZadbxHBzLi9tSqiCDTDe48JxdtRjrVasV1xDuYCgBzPRzrHq1S4dWUB9G1QrRURw4Ei1Ry5tnoTQamkYxXKMeJ55rubkAxbutjaRydHQZnYpgqUoSqtjqzNLtNKmLUh4CMWo2GiFKty2o7omPucfVJqjKj8p8NdLt34WaJLYNH51aqKhmTxVhJHwi8dD1AQtEdUxRCmTEUHtwo2xSqJ12xecvGwW68j4EwNZB3Vv2oksXXTXBPi2L5GsY4rQrVyfb72W3FGbgkpXXHB3SWZgMrNdezhyxhmEePYVm5gERYZovTaYoEcKkG1vbEMBmUA",

                    mode: String = "d", // d for default and c for custom
                  )

object ErgoLUPortExecutor extends App {
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
    // arguments are bad, error message will have been displayed
  }
}
