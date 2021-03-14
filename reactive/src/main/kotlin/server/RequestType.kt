package server

import model.Currency

sealed class RequestType
data class ShowFor(val name: String) : RequestType()
data class SignUp(val name: String, val currency: Currency) : RequestType()
data class AddProduct(val productName: String, val price: Double) : RequestType()
data class Error(val message: String) : RequestType()
