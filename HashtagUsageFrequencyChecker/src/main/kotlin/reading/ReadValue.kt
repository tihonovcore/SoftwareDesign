package reading

import com.google.gson.JsonElement

/**
 * Result of request. Uses in readers
 */
sealed class ReadValue

/**
 * Result of request, when nothing has read.
 */
object Empty : ReadValue()

/**
 * Result of request, when [json] has read, but not
 * returned from reader.
 */
data class New(val json: JsonElement) : ReadValue()

/**
 * Result of request, when [json] has read, and have
 * already returned from reader.
 */
data class Old(val json: JsonElement) : ReadValue()
