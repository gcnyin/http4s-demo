package example

import cats.effect.IO
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.zio._
import sttp.tapir.server.ServerEndpoint
import zio.json._

object Endpoints {
  final case class GetCounterResponse(value: Int)
  object GetCounterResponse {
    implicit val value: JsonCodec[GetCounterResponse] = DeriveJsonCodec.gen[GetCounterResponse]
  }

  final case class IncreaseAndCounterResponse(value: Int)
  object IncreaseAndCounterResponse {
    implicit val value: JsonCodec[IncreaseAndCounterResponse] = DeriveJsonCodec.gen[IncreaseAndCounterResponse]
  }

  final case class DecreaseAndCounterResponse(value: Int)

  object DecreaseAndCounterResponse {
    implicit val value: JsonCodec[DecreaseAndCounterResponse] = DeriveJsonCodec.gen[DecreaseAndCounterResponse]
  }


  val getEndpoint: Endpoint[Unit, Unit, Unit, GetCounterResponse, Any] =
    endpoint.get
      .in("get")
      .out(jsonBody[GetCounterResponse])

  val increaseAndGetEndpoint: Endpoint[Unit, Unit, Unit, IncreaseAndCounterResponse, Any] =
    endpoint.get
      .in("increase-and-get")
      .out(jsonBody[IncreaseAndCounterResponse])

  val decreaseAndGetEndpoint: Endpoint[Unit, Unit, Unit, DecreaseAndCounterResponse, Any] =
    endpoint.get
      .in("decrease-and-get")
      .out(jsonBody[DecreaseAndCounterResponse])

  val all: List[ServerEndpoint[Any, IO]] = List.empty
}
