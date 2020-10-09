import com.google.gson.JsonElement

inline class Date(val date: Long)

abstract class ResponseParser {
    abstract fun parseDates(json: JsonElement): List<Date>
    abstract fun parseNextPageRequest(json: JsonElement): String?
}

class ResponseParserVK : ResponseParser() {
    override fun parseDates(json: JsonElement): List<Date> {
        try {
            return json.asJsonObject["items"].asJsonArray.map { Date(it.asJsonObject["date"].asLong) }.toList()
        } catch (e: Exception) {
            throw e //TODO
        }
    }

    override fun parseNextPageRequest(json: JsonElement): String? {
        return json.asJsonObject["next_from"]?.asString
    }
}
