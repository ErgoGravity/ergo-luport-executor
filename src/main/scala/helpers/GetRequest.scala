package helpers

import io.circe.Json
import io.circe.parser.parse
import scalaj.http.{BaseHttp, HttpConstants, HttpOptions}

import scala.util.{Failure, Success, Try}

object GetRequest {
  def defaultOptions: Seq[HttpOptions.HttpOption] = Seq(
    HttpOptions.connTimeout(10000),
    HttpOptions.readTimeout(50000),
    HttpOptions.followRedirects(false)
  )
  object SUSYHttp extends BaseHttp(None, defaultOptions, HttpConstants.utf8, 4096, "Mozilla/5.0 (X11; OpenBSD amd64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36",
    true
  )


  private val defaultHeader: Seq[(String, String)] = Seq[(String, String)](("Accept", "application/json"))

  def httpGetWithError(url: String, headers: Seq[(String, String)] = defaultHeader): Either[Throwable, Json] = {
    Try {
      val responseReq = SUSYHttp(url).headers(defaultHeader).asString
      (responseReq.code, responseReq)
    }
    match {
      case Success((200, responseReq)) => parse(responseReq.body)
      case Success((responseHttpCode, responseReq)) => Left(new Exception(s"returned a error with http code $responseHttpCode and error ${responseReq.throwError}"))
      case Failure(exception) => Left(exception)
    }
  }

  def httpGet(url: String): Json = {
    httpGetWithError(url) match {
      case Right(json) => json
      case Left(ex) => throw ex
    }
  }

  def getDetails(url: String): Json = try {
    GetRequest.httpGet(url)
  }
  catch {
    case ex: Throwable => {
      println(ex)
      Json.Null
    }
  }

  def susyTokens(url: String): Map[String, String] = {
    val data = getDetails(url)
    if (data.isNull){
      throw new Throwable("can not get data from proxy")
    }
    val tokenIds = data.hcursor.downField("tokenIds").as[Map[String, String]].getOrElse(throw new Throwable("parse error"))

    tokenIds
  }
  def susyContracts(url: String): Map[String, String] = {
    val data = getDetails(url)
    if (data.isNull){
      throw new Throwable("can not get data from proxy")
    }
    val contractAddreses = data.hcursor.downField("contractAddreses").as[Map[String, String]].getOrElse(throw new Throwable("parse error"))

    contractAddreses
  }

}
