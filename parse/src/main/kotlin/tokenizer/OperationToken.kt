package tokenizer

import visitors.TokenVisitor

abstract class OperationToken : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

data class Plus(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken()

data class Subtract(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken()

data class Multiply(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken()

data class Divide(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken()
