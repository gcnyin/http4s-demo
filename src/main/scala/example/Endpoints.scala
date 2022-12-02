package example

import cats.effect.IO
import sttp.tapir._
import sttp.tapir.server.ServerEndpoint

object Endpoints {
  val getEndpoint: Endpoint[Unit, Unit, Unit, String, Any] =
    endpoint.get
      .in("get")
      .out(stringBody)

  val increaseAndGetEndpoint: Endpoint[Unit, Unit, Unit, String, Any] =
    endpoint.get
      .in("increase-and-get")
      .out(stringBody)

  val decreaseAndGetEndpoint: Endpoint[Unit, Unit, Unit, String, Any] =
    endpoint.get
      .in("decrease-and-get")
      .out(stringBody)

  val all: List[ServerEndpoint[Any, IO]] = List.empty
}
