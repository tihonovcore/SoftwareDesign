package visitors

import org.junit.Assert.assertEquals
import org.junit.Test
import tokenizer.Token
import tokenizer.Tokenizer

class TestCalcVisitor {
    @Test
    fun testEmptyString() {
        val input = ""
        val tokens = Tokenizer(input).getTokens()
        val visitor = CalcVisitor()
        visitor.visit(tokens)

        assertEquals(emptyList<Token>(), visitor.operands)
    }

    @Test
    fun testSingleNumber() {
        val input = "3004"
        val tokens = Tokenizer(input).getTokens()
        val visitor = CalcVisitor()
        visitor.visit(tokens)

        assertEquals(listOf(3004), visitor.operands)
    }

    @Test
    fun testOperation() {
        val input = "15 227 +"
        val tokens = Tokenizer(input).getTokens()
        val visitor = CalcVisitor()
        visitor.visit(tokens)

        assertEquals(listOf(242), visitor.operands)
    }

    @Test
    fun testFewOperations() {
        val input = "1 15 / 227 4 * +"
        val tokens = Tokenizer(input).getTokens()
        val visitor = CalcVisitor()
        visitor.visit(tokens)

        assertEquals(listOf(908), visitor.operands)
    }

    @Test
    fun testFewOperationsWithBraces() {
        val input = "2 2 + 313 4 5 - / *"
        val tokens = Tokenizer(input).getTokens()
        val visitor = CalcVisitor()
        visitor.visit(tokens)

        assertEquals(listOf(-1252), visitor.operands)
    }

    @Test
    fun testWhitespaces() {
        val input = "2 2 + 2 *"
        val tokens = Tokenizer(input).getTokens()
        val visitor = CalcVisitor()
        visitor.visit(tokens)

        assertEquals(listOf(8), visitor.operands)
    }
}
