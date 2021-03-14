package model

import org.bson.Document

data class Product(
    val name: String,
    val price: Price
) {
    constructor(document: Document) : this(
        document.getString("productName"),
        Price(document.getDouble("price"))
    )
}
