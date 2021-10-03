package helpers

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigHelper {
  val config: Config = ConfigFactory.load()

  def readobject(key: String): java.util.List[String] = {
    try {
      config.getStringList(key)
    } catch {
      case ex: Throwable =>
        println(s"${key} is required.")
        sys.exit()
    }
  }

  /**
   * Read the config and return the value of the key
   *
   * @param key     key to find
   * @param default default value if the key is not found
   * @return value of the key
   */
  def readKey(key: String, default: String = null): String = {
    try {
      config.getString(key)
    } catch {
      case ex: Throwable =>
        println(s"${key} is required.")
        sys.exit()
    }
  }

  def getTokens(key: String, default: String = null): Map[String, String]= {
    try {
      val url = readKey(key)
      val data = GetRequest.susyTokens(url)
      var tokens = Map[String, String]()
      data.foreach { case (key, value) =>
          if(key == "gwTokenId"){
            tokens += "tokeId" -> value
          }else{
            tokens += key -> value
          }
      }
      tokens
    } catch {
      case ex: Throwable =>
        println(ex.getMessage)
        sys.exit()
    }
  }
  def getContracts(key: String, default: String = null): Map[String, String]= {
    try {
      val url = readKey(key)
      val Contracts = GetRequest.susyContracts(url)
      Contracts
    } catch {
      case ex: Throwable =>
        println(ex.getMessage)
        sys.exit()
    }
  }
}
