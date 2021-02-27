package messages

sealed class Api(val request: String)
class Google(request: String) : Api(request)
class Yandex(request: String) : Api(request)
class Bing(request: String) : Api(request)
