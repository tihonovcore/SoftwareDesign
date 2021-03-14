package model

import model.Currency.*

data class Price(private val price: Double) {
    //TODO: no hardcode!
    fun `in`(currency: Currency) = when(currency) {
        USD -> price
        RUB -> price * 2
        EUR -> price * 0.8
    }
}
