package tokenizer

import visitors.TokenVisitor

data class NumberToken(
    override val text: String,
    override val from: Int,
    override val to: Int
) : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}
