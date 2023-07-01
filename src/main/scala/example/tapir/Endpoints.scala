package example.tapir

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

  final case class IncreaseAndGetResponse(value: Int)
  object IncreaseAndGetResponse {
    implicit val value: JsonCodec[IncreaseAndGetResponse] = DeriveJsonCodec.gen[IncreaseAndGetResponse]
  }

  final case class DecreaseAndGetResponse(value: Int)

  object DecreaseAndGetResponse {
    implicit val value: JsonCodec[DecreaseAndGetResponse] = DeriveJsonCodec.gen[DecreaseAndGetResponse]
  }

  val getEndpoint: Endpoint[Unit, Unit, Unit, GetCounterResponse, Any] =
    endpoint.get
      .in("counter" / "get")
      .out(jsonBody[GetCounterResponse])

  val increaseAndGetEndpoint: Endpoint[Unit, Unit, Unit, IncreaseAndGetResponse, Any] =
    endpoint.get
      .in("counter" / "increase-and-get")
      .out(jsonBody[IncreaseAndGetResponse])

  val decreaseAndGetEndpoint: Endpoint[Unit, Unit, Unit, DecreaseAndGetResponse, Any] =
    endpoint.get
      .in("counter" / "decrease-and-get")
      .out(jsonBody[DecreaseAndGetResponse])

  val all: List[ServerEndpoint[Any, IO]] = List.empty
}
