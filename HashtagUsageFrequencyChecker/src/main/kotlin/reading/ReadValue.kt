package reading

import com.google.gson.JsonElement

sealed class ReadValue
object Empty : ReadValue()
data class New(val json: JsonElement) : ReadValue()
data class Old(val json: JsonElement) : ReadValue()
