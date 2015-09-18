package module.dao

import org.slf4j.LoggerFactory
import org.specs2.mutable.Specification
import play.api.libs.ws.WS

import scala.util.{Failure, Success}

//Futures, Promises and goodies

class ApplicationITSpec extends Specification {
  val log = LoggerFactory.getLogger(this.getClass)

//  "Application" should {
//    "find all car adverts" in  {
//      val eventualResponse = WS.url("localhost:9000/carAdverts").get()
//      val result = for {
//        res <- eventualResponse
//      } yield res
//
//      result onComplete {
//        case Success(e) => log.info(e.toString)
//        case Failure(e) => //
//      }
//
//      success
//    }
//  }
}
