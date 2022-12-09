package example

import cats.effect.Ref
import cats.effect.kernel.Async
import cats.implicits._
import example.Endpoints._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.ServerEndpoint.Full

class ServerLogic[F[_]: Async](counterRef: Ref[F, Int]) {
  implicit def logger: Logger[F] = Slf4jLogger.getLogger[F]

  private val getServerEndpoint: Full[Unit, Unit, Unit, Unit, GetCounterResponse, Any, F] =
    getEndpoint.serverLogicSuccess { _ =>
      for {
        counter <- counterRef.get
        _ <- Logger[F].info(s"counter get: $counter")
      } yield GetCounterResponse(counter)
    }

  private val increaseAndGetServerEndpoint: Full[Unit, Unit, Unit, Unit, IncreaseAndGetResponse, Any, F] =
    increaseAndGetEndpoint.serverLogicSuccess { _ =>
      for {
        counter <- counterRef.updateAndGet(_ + 1)
        _ <- Logger[F].info(s"counter increaseAndGet: $counter")
      } yield IncreaseAndGetResponse(counter)
    }

  private val decreaseAndGetServerEndpoint: Full[Unit, Unit, Unit, Unit, DecreaseAndGetResponse, Any, F] =
    decreaseAndGetEndpoint.serverLogicSuccess { _ =>
      for {
        counter <- counterRef.updateAndGet(_ - 1)
        _ <- Logger[F].info(s"counter decreaseAndGet: $counter")
      } yield DecreaseAndGetResponse(counter)
    }

  val all: List[ServerEndpoint[Any, F]] = List(getServerEndpoint, increaseAndGetServerEndpoint, decreaseAndGetServerEndpoint)
}
