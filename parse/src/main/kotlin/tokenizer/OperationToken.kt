package tokenizer

import visitors.TokenVisitor

sealed class OperationToken : Token {
    abstract val priority: Int
    abstract val renderValue: String

    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

data class Plus(
    override val text: String,
    override val from: Int,
    override val to: Int,
) : OperationToken()  {
    override val priority: Int = 0
    override val renderValue = "PLUS"
}

data class Subtract(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken() {
    override val priority: Int = 0
    override val renderValue = "SUBTRACT"
}

data class Multiply(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken() {
    override val priority: Int = 1
    override val renderValue = "MULTIPLY"
}

data class Divide(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken() {
    override val priority: Int = 1
    override val renderValue = "DIVIDE"
}
