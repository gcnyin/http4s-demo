package example

import cats.effect.{IO, Ref}
import example.Endpoints.{decreaseAndGetEndpoint, getEndpoint, increaseAndGetEndpoint}
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ServerEndpoint.Full

class ServerLogic(counterRef: Ref[IO, Int]) {
  private val getServerEndpoint: Full[Unit, Unit, Unit, Unit, String, Any, IO] =
    getEndpoint.serverLogicSuccess(_ => counterRef.get.map(_.toString))

  private val increaseAndGetServerEndpoint: Full[Unit, Unit, Unit, Unit, String, Any, IO] =
    increaseAndGetEndpoint.serverLogicSuccess(_ => counterRef.updateAndGet(_ + 1).map(_.toString))

  private val decreaseAndGetServerEndpoint: Full[Unit, Unit, Unit, Unit, String, Any, IO] =
    decreaseAndGetEndpoint.serverLogicSuccess(_ => counterRef.updateAndGet(_ - 1).map(_.toString))

  val all: List[ServerEndpoint[Any, IO]] = List(getServerEndpoint, increaseAndGetServerEndpoint, decreaseAndGetServerEndpoint)
}
