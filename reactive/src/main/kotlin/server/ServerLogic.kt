package server

import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.RequestHandler
import rx.Observable

object ServerLogic : RequestHandler<ByteBuf, ByteBuf> {
    override fun handle(req: HttpServerRequest<ByteBuf?>, resp: HttpServerResponse<ByteBuf?>): Observable<Void> {
        val path = req.decodedPath.split('/').drop(1)
        val response = when(val action = parseRequest(path)) {
            is SignUp -> {
                val (name, currency) = action

                DatabaseLogic
                    .addUser(name, currency)
                    .map { "user $name signed up" }
            }
            is AddProduct -> {
                val (productName, price) = action

                DatabaseLogic
                    .addProduct(productName, price)
                    .map { "product $productName with price $price added" }
            }
            is ShowFor -> {
                DatabaseLogic
                    .getUser(action.name)
                    .flatMap { user ->
                        DatabaseLogic
                            .getProducts()
                            .map { product ->
                                val price = product.price.`in`(user.currency)
                                "${product.name} \t $price\n"
                            }
                    }
            }
            is Error -> Observable.just(action.message)
        }
        return resp.writeString(response)
    }
}
