package model

import org.bson.Document
import server.parseCurrency

data class User(
    val name: String,
    val currency: Currency
) {
    constructor(document: Document) : this(
        document.getString("name"),
        parseCurrency(document.getString("currency"))!!
    )
}

