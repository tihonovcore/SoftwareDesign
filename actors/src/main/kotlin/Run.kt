import AggregationConfig.Companion.TIMEOUT
import actors.Aggregator
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import messages.Request

fun aggregate(request: String): Set<String> {
    val system = ActorSystem.create("aggregate")
    val answers = mutableSetOf<String>()
    val kernel = system.actorOf(Props.create(Aggregator::class.java, answers), "aggregator")
    kernel.tell(Request(request), ActorRef.noSender())

    Thread.sleep(TIMEOUT)
    return answers
}
