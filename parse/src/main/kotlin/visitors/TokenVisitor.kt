package visitors

import tokenizer.BraceToken
import tokenizer.NumberToken
import tokenizer.OperationToken
import tokenizer.Token

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: BraceToken)
    fun visit(token: OperationToken)
    fun visit(tokens: List<Token>): List<Token>
}
