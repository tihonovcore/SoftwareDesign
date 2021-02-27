package actors

import AggregationConfig.Companion.COUNT_API
import AggregationConfig.Companion.TIMEOUT
import messages.*
import akka.actor.*
import akka.japi.pf.ReceiveBuilder
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class Aggregator(
    private val responses: MutableSet<String>
) : AbstractActor() {

    override fun createReceive(): Receive = ReceiveBuilder()
            .match(Request::class.java, this::onRequest)
            .match(Response::class.java, this::onResponse)
            .match(ReceiveTimeout::class.java, this::onReceiveTimeout)
            .build()

    private fun onRequest(msg: Request) {
        context.actorOf(Props.create(ApiWorker::class.java)).tell(Google(msg.request), self)
        context.actorOf(Props.create(ApiWorker::class.java)).tell(Yandex(msg.request), self)
        context.actorOf(Props.create(ApiWorker::class.java)).tell(Bing(msg.request), self)
        context.setReceiveTimeout(Duration.create(TIMEOUT, TimeUnit.MILLISECONDS))
    }

    private fun onResponse(msg: Response) {
        responses += msg.response.map { it.drop(1).dropLast(1) }

        if (responses.size == COUNT_API) {
            println(responses)
            self.tell(PoisonPill.getInstance(), self)
        }
    }

    private fun onReceiveTimeout(msg: ReceiveTimeout) {
        println("time is out")
        println(responses)
        self.tell(PoisonPill.getInstance(), self)
    }
}
