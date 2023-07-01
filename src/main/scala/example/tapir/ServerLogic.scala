package example.tapir

import cats.effect.Ref
import cats.effect.kernel.Async
import cats.syntax.functor._
import example.tapir.Endpoints._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ServerEndpoint.Full

class ServerLogic[F[_]: Async](counterRef: Ref[F, Int]) {
  private val getServerEndpoint: Full[Unit, Unit, Unit, Unit, GetCounterResponse, Any, F] =
    getEndpoint.serverLogicSuccess(_ => counterRef.get.map(GetCounterResponse(_)))

  private val increaseAndGetServerEndpoint: Full[Unit, Unit, Unit, Unit, IncreaseAndGetResponse, Any, F] =
    increaseAndGetEndpoint.serverLogicSuccess(_ => counterRef.updateAndGet(_ + 1).map(IncreaseAndGetResponse(_)))

  private val decreaseAndGetServerEndpoint: Full[Unit, Unit, Unit, Unit, DecreaseAndGetResponse, Any, F] =
    decreaseAndGetEndpoint.serverLogicSuccess(_ => counterRef.updateAndGet(_ - 1).map(DecreaseAndGetResponse(_)))

  val all: List[ServerEndpoint[Any, F]] = List(getServerEndpoint, increaseAndGetServerEndpoint, decreaseAndGetServerEndpoint)
}
