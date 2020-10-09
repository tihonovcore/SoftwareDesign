package parsing

import com.google.gson.JsonElement

abstract class ResponseParser {
    abstract fun parseDates(json: JsonElement): List<Date>
    abstract fun parseNextPageRequest(json: JsonElement): String?
}

inline class Date(val date: Long)
