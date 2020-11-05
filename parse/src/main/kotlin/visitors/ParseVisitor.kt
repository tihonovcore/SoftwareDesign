package visitors

import tokenizer.*
import java.lang.IllegalStateException
import java.util.*

class ParseVisitor : TokenVisitor {
    val postfixNotation = mutableListOf<Token>()
    private val operations = Stack<Token>()

    override fun visit(token: NumberToken) {
        postfixNotation += token
    }

    override fun visit(token: BraceToken) {
        if (token is Open) {
            operations += token
        } else if (token is Close) {
            do {
                if (operations.isEmpty()) {
                    throw IllegalStateException("No open brace for $token")
                }

                when (val top = operations.pop()) {
                    is OperationToken -> postfixNotation += top
                    is Open -> break
                    else -> throw IllegalStateException("Unexpected token: $top")
                }
            } while (true)
        }
    }

    override fun visit(token: OperationToken) {
        while (operations.isNotEmpty() && operations.peek() higher token) {
            postfixNotation += operations.pop()
        }
        operations += token
    }

    override fun visit(tokens: List<Token>): List<Token> {
        tokens.forEach { it.accept(this) }

        while (operations.isNotEmpty()) {
            val top = operations.pop()
            if (top !is OperationToken) {
                throw IllegalStateException("list")
            }
            postfixNotation += top
        }

        return postfixNotation
    }

    private infix fun Token.higher(other: Token): Boolean {
        if (this is OperationToken && other is OperationToken) {
            return priority >= other.priority
        }

        return false
    }
}
