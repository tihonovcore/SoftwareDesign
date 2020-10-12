package parsing

import com.google.gson.JsonElement
import java.lang.IllegalStateException

/**
 * Gets useful information from VK's responses.
 */
open class ResponseParserVK : ResponseParser() {
    override fun parseDates(json: JsonElement): List<Date> {
        try {
            val items = json.response().asJsonObject["items"].asJsonArray
            return items.map { it.asJsonObject["date"].asLong }
        } catch (e: Exception) {
            throw IllegalStateException("Bad response: $json")
        }
    }

    override fun parseNextPageRequest(json: JsonElement): String? {
        return json.response().asJsonObject["next_from"]?.asString
    }

    private fun JsonElement.response(): JsonElement {
        return asJsonObject["response"]
    }
}
