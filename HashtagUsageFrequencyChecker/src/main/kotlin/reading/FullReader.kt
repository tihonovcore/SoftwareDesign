package reading

import com.google.gson.JsonElement
import java.lang.IllegalStateException

class FullReader(
    val hashtag: String,
    val startTime: Long,
    val endTime: Long,
    private val coreReader: CoreReader
){
    private var lastReadValue: ReadValue = Empty

    fun hasNext(): Boolean {
        lastReadValue = coreReader.tryToRead(lastReadValue, hashtag, startTime, endTime)

        return when (lastReadValue) {
            is New -> true
            else -> false
        }
    }

    fun read(): JsonElement {
        lastReadValue = coreReader.tryToRead(lastReadValue, hashtag, startTime, endTime)

        val currentValue = lastReadValue
        if (currentValue is New) {
            lastReadValue = Old(currentValue.json)
            return currentValue.json
        }

        throw IllegalStateException("Reading ended")
    }
}
