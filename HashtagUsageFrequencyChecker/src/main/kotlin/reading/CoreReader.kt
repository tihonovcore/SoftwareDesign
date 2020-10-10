package reading

interface CoreReader {
    fun tryToRead(
        lastReadValue: ReadValue,
        hashtag: String,
        startTime: Long,
        endTime: Long
    ): ReadValue
}
