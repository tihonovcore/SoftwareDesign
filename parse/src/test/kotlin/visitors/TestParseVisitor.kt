package visitors

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import tokenizer.*

class TestParseVisitor {
    @Test
    fun testEmpty() {
        val input = ""
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(listOf<Token>(), postfixNotation)
    }

    @Test
    fun testNumber() {
        val input = "457"
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(
            listOf(
                NumberToken("457", 0, 3)
            ),
            postfixNotation
        )
    }

    @Test
    fun testOperation() {
        val input = "8*7"
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(
            listOf(
                NumberToken("8", 0, 1),
                NumberToken("7", 2, 3),
                Multiply("*", 1, 2),
            ),
            postfixNotation
        )
    }

    @Test
    fun testOperationsWithDifferentPriority() {
        val input = "2+2*2"
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(
            listOf(
                NumberToken("2", 0, 1),
                NumberToken("2", 2, 3),
                NumberToken("2", 4, 5),
                Multiply("*", 3, 4),
                Plus("+", 1, 2)
            ),
            postfixNotation
        )
    }

    @Test
    fun testOperationsWithDifferentPriority2() {
        val input = "2*2+2"
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(
            listOf(
                NumberToken("2", 0, 1),
                NumberToken("2", 2, 3),
                Multiply("*", 1, 2),
                NumberToken("2", 4, 5),
                Plus("+", 3, 4)
            ),
            postfixNotation
        )
    }

    @Test
    fun testBraces() {
        val input = "(3+3)*3"
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(
            listOf(
                NumberToken("3", 1, 2),
                NumberToken("3", 3, 4),
                Plus("+", 2, 3),
                NumberToken("3", 6, 7),
                Multiply("*", 5, 6)
            ),
            postfixNotation
        )
    }

    @Test
    fun testBigSequence() {
        val input = "3+4*2/(1-5)-12"
        val tokens = Tokenizer(input).getTokens()
        val postfixNotation = ParseVisitor().visit(tokens)

        assertEquals(
            listOf(
                NumberToken("3", 0, 1),
                NumberToken("4", 2, 3),
                NumberToken("2", 4, 5),
                Multiply("*", 3, 4),
                NumberToken("1", 7, 8),
                NumberToken("5", 9, 10),
                Subtract("-", 8, 9),
                Divide("/", 5, 6),
                Plus("+", 1, 2),
                NumberToken("12", 12, 14),
                Subtract("-", 11, 12)
            ),
            postfixNotation
        )
    }


    @Test
    fun testNoOpenBrace() {
        val input = "4+)4"
        val tokens = Tokenizer(input).getTokens()

        try {
            ParseVisitor().visit(tokens)
        } catch (_: IllegalStateException) {
            return
        }
        assert(false)
    }

    @Test
    fun testNoCloneBrace() {
        val input = "5-(4+3"
        val tokens = Tokenizer(input).getTokens()

        try {
            ParseVisitor().visit(tokens)
        } catch (_: IllegalStateException) {
            return
        }
        assert(false)
    }

    @Ignore("hard to detect")
    @Test
    fun testNoOperation() {
        val input = "8 7"
        val tokens = Tokenizer(input).getTokens()

        try {
            ParseVisitor().visit(tokens)
        } catch (_: IllegalStateException) {
            return
        }
        assert(false)
    }

    @Ignore("hard to detect")
    @Test
    fun testNoOperand() {
        val input = "4+"
        val tokens = Tokenizer(input).getTokens()

        try {
            ParseVisitor().visit(tokens)
        } catch (_: IllegalStateException) {
            return
        }
        assert(false)
    }
}
