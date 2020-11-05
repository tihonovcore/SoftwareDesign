package tokenizer

import visitors.TokenVisitor

abstract class OperationToken : Token {
    abstract val priority: Int

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
}

data class Subtract(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken() {
    override val priority: Int = 0
}

data class Multiply(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken() {
    override val priority: Int = 1
}

data class Divide(
    override val text: String,
    override val from: Int,
    override val to: Int
) : OperationToken() {
    override val priority: Int = 1
}
