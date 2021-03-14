import io.reactivex.netty.protocol.http.server.HttpServer
import server.ServerLogic

fun main() {
    HttpServer
        .newServer(8080)
        .start(ServerLogic)
        .awaitShutdown()
}
