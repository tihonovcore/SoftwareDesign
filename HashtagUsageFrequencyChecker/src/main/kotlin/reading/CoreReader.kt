package reading

/**
 * Makes specific request to server and returns result.
 */
interface CoreReader {
    /**
     * Makes specific request to server and returns result,
     * boxed in [ReadValue].
     *
     * @param lastReadValue uses for make request.
     * @param hashtag specifies hashtag.
     * @param startTime specifies start time of searching
     * @param endTime specifies start time of searching
     *
     * @return If [lastReadValue] is [Empty], then makes request with
     * [startTime] and [endTime]. If [lastReadValue] is [New], do nothing
     * and return it back. In case [lastReadValue] of [Old] tries to read
     * new page, uses [Old.json]. On success returns it into [New], on
     * failure returns back [Old].
     */
    fun tryToRead(
        lastReadValue: ReadValue,
        hashtag: String,
        startTime: Long,
        endTime: Long
    ): ReadValue
}
