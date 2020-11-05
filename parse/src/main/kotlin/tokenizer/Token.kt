package tokenizer

import visitors.TokenVisitor

interface Token {
    val text: String
    val from: Int
    val to: Int

    fun accept(visitor: TokenVisitor)
}
