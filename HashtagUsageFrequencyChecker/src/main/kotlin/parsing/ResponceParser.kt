package parsing

import com.google.gson.JsonElement

typealias Date = Long

abstract class ResponseParser {
    abstract fun parseDates(json: JsonElement): List<Date>
    abstract fun parseNextPageRequest(json: JsonElement): String?
}
