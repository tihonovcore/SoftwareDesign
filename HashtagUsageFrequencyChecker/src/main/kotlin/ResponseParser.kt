import com.google.gson.JsonElement
import com.google.gson.JsonParser

//split to abstract RP and RP_VK (or RP_Twitter, ...)
class ResponseParser(
    val json: JsonElement
) {
    //так не надо
    constructor(response: String) : this(JsonParser.parseString(response).asJsonObject["response"])

    fun parseTotalCount(): Int {
        try {
            return json.asJsonObject["total_count"].asInt
        } catch (e: Exception) {
            throw e //TODO
        }
    }

    fun parseDates(): List<Item> {
        try {
            return json.asJsonObject["items"].asJsonArray.map { Item(it.asJsonObject["date"].asLong) }.toList()
        } catch (e: Exception) {
            throw e //TODO
        }
    }

    fun parseNextFrom(): String? {
        return json.asJsonObject["next_from"]?.asString
    }
}

//на самом деле это Date
inline class Item(val date: Long)
