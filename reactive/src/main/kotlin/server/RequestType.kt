package server

import model.Currency

sealed class RequestType
class ShowFor(val userId: Int) : RequestType()
class SignUp(val name: String, val currency: Currency) : RequestType()
class AddProduct(val productName: String, val price: Double) : RequestType()
class Error(val message: String) : RequestType()
