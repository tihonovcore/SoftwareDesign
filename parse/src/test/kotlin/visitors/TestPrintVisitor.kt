package visitors

import org.junit.Assert.assertEquals
import org.junit.Test
import tokenizer.Tokenizer
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestPrintVisitor {
    @Test
    fun testEmptyString() {
        val input = ""
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(output =  PrintStream(outputStream)).visit(tokens)

        assertEquals("", outputStream.toString())
    }

    @Test
    fun testSingleNumber() {
        val input = "3004"
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(output =  PrintStream(outputStream)).visit(tokens)

        assertEquals("NUMBER(3004) ", outputStream.toString())
    }

    @Test
    fun testOperation() {
        val input = "15+227"
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(output =  PrintStream(outputStream)).visit(tokens)

        assertEquals("NUMBER(15) PLUS NUMBER(227) ", outputStream.toString())
    }

    @Test
    fun testFewOperations() {
        val input = "1/15+227*4"
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(output =  PrintStream(outputStream)).visit(tokens)

        assertEquals("NUMBER(1) DIVIDE NUMBER(15) PLUS NUMBER(227) MULTIPLY NUMBER(4) ", outputStream.toString())
    }

    @Test
    fun testFewOperationsWithBraces() {
        val input = "(2+2)*313/(4-5)"
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(output =  PrintStream(outputStream)).visit(tokens)

        val left = "OPEN_BRACE NUMBER(2) PLUS NUMBER(2) CLOSE_BRACE"
        val right = "OPEN_BRACE NUMBER(4) SUBTRACT NUMBER(5) CLOSE_BRACE"
        assertEquals("$left MULTIPLY NUMBER(313) DIVIDE $right ", outputStream.toString())
    }

    @Test
    fun testWhitespaces() {
        val input = " (  \n2   \t+ 2 \r) * 2  \n  "
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(output =  PrintStream(outputStream)).visit(tokens)

        val two = "NUMBER(2)"
        assertEquals("OPEN_BRACE $two PLUS $two CLOSE_BRACE MULTIPLY $two ", outputStream.toString())
    }

    @Test
    fun testPrintPositions() {
        val input = "14+87"
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(printPositions = true, output =  PrintStream(outputStream)).visit(tokens)

        assertEquals("NUMBER(14)_AT_0 PLUS_AT_2 NUMBER(87)_AT_3 ", outputStream.toString())
    }

    @Test
    fun testPrintPositions2() {
        val input = "  \n2   \t+ 2 \r"
        val tokens = Tokenizer(input).getTokens()
        val outputStream = ByteArrayOutputStream()
        PrintVisitor(printPositions = true, output =  PrintStream(outputStream)).visit(tokens)

        assertEquals("NUMBER(2)_AT_3 PLUS_AT_8 NUMBER(2)_AT_10 ", outputStream.toString())
    }
}
