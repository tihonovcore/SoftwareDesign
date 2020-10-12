package parsing

import com.google.gson.JsonElement

/**
 * Date in unixtime format.
 */
typealias Date = Long

/**
 * Gets some useful information from Json response.
 */
abstract class ResponseParser {
    /**
     * Get publication dates of all posts from specified [json].
     */
    abstract fun parseDates(json: JsonElement): List<Date>

    /**
     * Get info about next page for next request from
     * specified [json].
     *
     * @return String with info. If current page is last,
     * return null.
     */
    abstract fun parseNextPageRequest(json: JsonElement): String?
}
