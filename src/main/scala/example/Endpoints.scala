package example

import cats.effect.IO
import sttp.tapir._
import sttp.tapir.server.ServerEndpoint

object Endpoints {
  final case class User(name: String) extends AnyVal

  private val helloEndpoint: PublicEndpoint[User, Unit, String, Any] =
    endpoint.get
      .in("hello")
      .in(query[User]("name"))
      .out(stringBody)

  private val helloServerEndpoint: ServerEndpoint[Any, IO] =
    helloEndpoint.serverLogicSuccess(user => IO.pure(s"Hello ${user.name}"))

  val all: List[ServerEndpoint[Any, IO]] = List(helloServerEndpoint)
}
