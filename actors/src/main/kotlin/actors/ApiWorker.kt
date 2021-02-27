package actors

import akka.actor.AbstractActor
import akka.japi.pf.ReceiveBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import messages.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class ApiWorker : AbstractActor() {
    override fun createReceive(): Receive = ReceiveBuilder()
            .match(Api::class.java, this::onResponse)
            .build()

    private fun onResponse(msg: Api) {
        val url = when (msg) {
            is Google -> URL("http://localhost:32543/google/com/${msg.request}")
            is Yandex -> URL("http://localhost:32543/yandex/com/${msg.request}")
            is Bing -> URL("http://localhost:32543/bing/com/${msg.request}")
        }
        val json = BufferedReader(InputStreamReader(url.openStream())).use {
            val text = it.readText()
            JsonParser.parseString(text).process()
        }

        sender.tell(Response(json), self)
    }

    private fun JsonElement.process(): List<String> {
        return asJsonObject["answers"].asJsonArray.take(5).map { it.toString() }
    }
}
