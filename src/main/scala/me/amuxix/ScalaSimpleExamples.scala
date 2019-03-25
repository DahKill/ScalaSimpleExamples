package me.amuxix
import java.io.{File, PrintWriter}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.data.OptionT
import cats.implicits._
import play.api.libs.json.JsValue
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.StandaloneWSResponse
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}

object ScalaSimpleExamples {
  //An execution context is basically a thread pool scala uses for asynchronous computations
  implicit val ec = ExecutionContext.global

  def getActorSystemAndWsClient: (ActorSystem, StandaloneAhcWSClient) = {
    // Create Akka system for thread and streaming management
    implicit val system = ActorSystem()
    system.registerOnTermination {
      System.exit(0)
    }
    implicit val materializer = ActorMaterializer()

    val client = StandaloneAhcWSClient(httpCache = None)
    (system, client)
  }

  def longComputation(i: Int): Future[Int] = Future {
    Thread.sleep(1000)
    println(i)
    i
  }

  def superLongComputation(i: Int): Future[Int] = Future {
    Thread.sleep(5000)
    i
  }

  def blockingLongComputation(i: Int): Int = {
    Thread.sleep(1000)
    println(i)
    i
  }

  val (system, client) = getActorSystemAndWsClient
  def main(args: Array[String]): Unit = {
    /*val future: Future[Int] = Future(throw new Exception)
    val int = Await.result(future, Duration.Inf)*/

    val futures: Seq[Future[Int]] = (1 to 10).map(i => longComputation(i)) :+ superLongComputation(1)
    Future.sequence(futures).map { seq =>
      println(seq.sum)
    }

    (1 to 10).par.map(blockingLongComputation)
    val response: Future[StandaloneWSResponse] = client
      .url("https://poe.ninja/api/data/currencyoverview")
      .withQueryStringParameters(
        "league" -> "Standard",
        "type" -> "Currency",
      )
      .get()

    val response2 = client
      .url("https://poe.ninja/api/data/currencyoverview")
      .withQueryStringParameters(
        "league" -> "Standard",
        "type" -> "Currency",
      )
      .get()

    val either: Either[String, Int] = Right(1)

    val futureOption: Future[Option[Currency]] = response.map { response =>
      response
        .body[JsValue]
        .validate[Currency]
        .asOpt
    }

    val optionT: OptionT[Future, Currency] = OptionT(futureOption)
    optionT.map(currency => println(currency.lines))

    futureOption
      .onComplete { _ =>
        client.close()
        system.terminate()
      }
    //response.flatMap(_ => response2)
    val futureInt = Future(1)
    futureInt.map(_ * 2)
    futureInt.flatMap(valor => Future(valor * 2))
    /*for {
      valor <- futureInt
      valor2 <- futureInt2
    } yield valor * 2*/
    Future.sequence(Seq(response, response2))
  }

  /**
    * This method method writes to a file named filename (creating it if it does not exist)
    * @param location
    * @param fileName
    * @param content
    */
  def createFile(location: String, fileName: String, content: String) = Future {
    val file = new PrintWriter(new File(location + fileName))
    file.write(content)
    file.close()
  }
}
