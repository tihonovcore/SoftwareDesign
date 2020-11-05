package tokenizer

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalStateException

class TestTokenizer {
    @Test
    fun testEmptyString() {
        val input = ""
        val tokenizer = Tokenizer(input)

        assertEquals(emptyList<Token>(), tokenizer.getTokens())
    }

    @Test
    fun testSingleNumber() {
        val input = "3004"
        val tokenizer = Tokenizer(input)

        assertEquals(
            listOf(NumberToken(input, 0, 4)),
            tokenizer.getTokens()
        )
    }

    @Test
    fun testOperation() {
        val input = "15+227"
        val tokenizer = Tokenizer(input)

        assertEquals(
            listOf(
                NumberToken("15", 0, 2),
                Plus("+", 2, 3),
                NumberToken("227", 3, 6),
            ),
            tokenizer.getTokens()
        )
    }

    @Test
    fun testFewOperations() {
        val input = "15+227"
        val tokenizer = Tokenizer(input)

        assertEquals(
            listOf(
                NumberToken("15", 0, 2),
                Plus("+", 2, 3),
                NumberToken("227", 3, 6),
            ),
            tokenizer.getTokens()
        )
    }

    @Test
    fun testFewOperationsWithBraces() {
        val input = "(2+2)*313/(4-5)"
        val tokenizer = Tokenizer(input)

        assertEquals(
            listOf(
                Open("(", 0, 1),
                NumberToken("2", 1, 2),
                Plus("+", 2, 3),
                NumberToken("2", 3, 4),
                Close(")", 4, 5),
                Multiply("*", 5, 6),
                NumberToken("313", 6, 9),
                Divide("/", 9, 10),
                Open("(", 10, 11),
                NumberToken("4", 11, 12),
                Subtract("-", 12, 13),
                NumberToken("5", 13, 14),
                Close(")", 14, 15),
            ),
            tokenizer.getTokens()
        )
    }

    @Test
    fun testWhitespaces() {
        val input = " (  \n2   \t+ 2 \r) * 2  \n  "
        val tokenizer = Tokenizer(input)

        assertEquals(
            listOf(
                Open("(", 1, 2),
                NumberToken("2", 5, 6),
                Plus("+", 10, 11),
                NumberToken("2", 12, 13),
                Close(")", 15, 16),
                Multiply("*", 17, 18),
                NumberToken("2", 19, 20),
            ),
            tokenizer.getTokens()
        )
    }

    @Test
    fun testUnexpectedSymbol() {
        val input = "♥val♥"
        val tokenizer = Tokenizer(input)

        try {
            tokenizer.getTokens()
        } catch (_: IllegalStateException) {
            return
        }
        assert(false)
    }
}
