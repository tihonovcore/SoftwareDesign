package visitors

import tokenizer.*
import java.io.PrintStream

class PrintVisitor(
    val printPositions: Boolean = false,
    val output: PrintStream = System.out
) : TokenVisitor {
    override fun visit(token: NumberToken) {
        printToken("NUMBER(${token.text})", token.from)
    }

    override fun visit(token: BraceToken) {
        val brace = if (token is Open) "OPEN_BRACE" else "CLOSE_BRACE"
        printToken(brace, token.from)
    }

    override fun visit(token: OperationToken) {
        printToken(token.renderValue, token.from)
    }

    override fun visit(tokens: List<Token>): List<Token> {
        tokens.forEach { it.accept(this) }
        return tokens
    }

    private fun printToken(name: String, position: Int) {
        output.print(name)
        if (printPositions) output.print("_AT_$position")
        output.print(" ")
    }
}
