package server

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.RequestHandler
import rx.Observable

object ServerLogic : RequestHandler<ByteBuf, ByteBuf> {
    override fun handle(req: HttpServerRequest<ByteBuf?>, resp: HttpServerResponse<ByteBuf?>): Observable<Void> {
        val name = req.decodedPath.drop(1)
        val response = Observable
            .just(name)
            .map { "Hello, $it!" }
        return resp.writeString(response)
    }
}
