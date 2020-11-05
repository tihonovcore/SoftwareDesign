package visitors

import tokenizer.*
import java.lang.IllegalArgumentException
import java.util.*

class CalcVisitor : TokenVisitor {
    val operands = Stack<Int>()

    override fun visit(token: NumberToken) {
        operands += Integer.parseInt(token.text)
    }

    override fun visit(token: BraceToken) {
        throw IllegalArgumentException("Expected expression in postfix form. Braces are not supported here")
    }

    override fun visit(token: OperationToken) {
        val right = operands.pop()
        val left = operands.pop()

        operands += op(left, right, token)
    }

    override fun visit(tokens: List<Token>): List<Token> {
        operands.clear()
        tokens.forEach { it.accept(this) }
        return tokens
    }

    private fun op(left: Int, right: Int, token: OperationToken): Int {
        return when(token) {
            is Plus -> left + right
            is Subtract -> left - right
            is Multiply -> left * right
            is Divide -> left / right
        }
    }
}
