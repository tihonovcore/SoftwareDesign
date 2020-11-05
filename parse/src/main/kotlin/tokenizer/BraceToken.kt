package tokenizer

import visitors.TokenVisitor

abstract class BraceToken : Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

data class Open(
    override val text: String,
    override val from: Int,
    override val to: Int
) : BraceToken()

data class Close(
    override val text: String,
    override val from: Int,
    override val to: Int
) : BraceToken()
