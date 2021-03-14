package server

import model.Currency

fun parseRequest(path: List<String>): RequestType {
    return when(path.first()) {
        "signup" -> {
            assert(path.size == 3) { return Error("Wrong request") }
            val name = path[1]
            val currency = parseCurrency(path[2]) ?: return Error("Bad currency: ${path[2]}")
            SignUp(name, currency)
        }
        "add_product" -> {
            assert(path.size == 3) { return Error("Wrong request") }
            val productName = path[1]
            val price = path[2].toDoubleOrNull() ?: return Error("Bad price: ${path[2]}")
            AddProduct(productName, price)
        }
        "show_for" -> {
            assert(path.size == 2) { return Error("Wrong request") }
            ShowFor(name = path[1])
        }
        else -> Error("Unexpected action: ${path.first()}")
    }
}

fun parseCurrency(currency: String): Currency? {
    return when(currency.toLowerCase()) {
        "usd" -> Currency.USD
        "rub" -> Currency.RUB
        "eur" -> Currency.EUR
        else -> null
    }
}
