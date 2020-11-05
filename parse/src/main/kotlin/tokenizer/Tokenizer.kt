package tokenizer

import java.lang.IllegalStateException

interface State {
    val tokenizer: Tokenizer
    fun readToken()

    fun decideNextState(): State {
        while (tokenizer.currentPosition < tokenizer.input.length && tokenizer.input[tokenizer.currentPosition].isWhitespace()) {
            tokenizer.currentPosition++
        }

        if (tokenizer.currentPosition == tokenizer.input.length) {
            return EndState(tokenizer)
        }

        fun Char.isBrace() = this in listOf('(', ')')
        fun Char.isOperation() = this in listOf('+', '-', '*', '/')

        val symbol = tokenizer.input[tokenizer.currentPosition]
        return when {
            symbol.isDigit() -> ReadNumberState(tokenizer)
            symbol.isBrace() -> ReadBraceState(tokenizer)
            symbol.isOperation() -> ReadOperationState(tokenizer)
            else -> {
                val errorMessage = "Unexpected character: $symbol at position ${tokenizer.currentPosition}"
                throw IllegalStateException(errorMessage)
            }
        }
    }
}

private class StartState(
    override val tokenizer: Tokenizer
) : State {
    override fun readToken() {
        tokenizer.state = decideNextState()
    }
}

private class ReadNumberState(
    override val tokenizer: Tokenizer
) : State {
    override fun readToken() = with(tokenizer) {
        val startPosition = currentPosition
        while (currentPosition < input.length && input[currentPosition].isDigit()) {
            currentPosition++
        }

        val text = input.substring(startPosition, currentPosition)
        tokens += NumberToken(text, startPosition, currentPosition)

        state = decideNextState()
    }
}

private class ReadBraceState(
    override val tokenizer: Tokenizer
) : State {
    override fun readToken() = with(tokenizer) {
        val startPosition = currentPosition++

        tokens += when (val text = input[startPosition].toString()) {
            "(" -> Open(text, startPosition, currentPosition)
            ")" -> Close(text, startPosition, currentPosition)
            else -> throw IllegalStateException("Internal error")
        }

        state = decideNextState()
    }
}

private class ReadOperationState(
    override val tokenizer: Tokenizer
) : State {
    override fun readToken() = with(tokenizer) {
        val startPosition = currentPosition++

        tokens += when (val text = input[startPosition].toString()) {
            "+" -> Plus(text, startPosition, currentPosition)
            "-" -> Subtract(text, startPosition, currentPosition)
            "*" -> Multiply(text, startPosition, currentPosition)
            "/" -> Divide(text, startPosition, currentPosition)
            else -> throw IllegalStateException("Internal error")
        }

        state = decideNextState()
    }
}

private class EndState(
    override val tokenizer: Tokenizer
) : State {
    override fun readToken() {
        //do nothing
    }
}

class Tokenizer(
    val input: String
) {
    internal val tokens = mutableListOf<Token>()

    internal var state: State = StartState(this)
    internal var currentPosition = 0

    fun getTokens(): List<Token> {
        do {
            state.readToken()
        } while (state !is EndState)

        return tokens
    }
}
