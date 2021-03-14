import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import model.Currency
import model.Product
import model.User
import org.bson.Document
import rx.Observable

object DatabaseLogic {
    private val client = MongoClients.create("mongodb://localhost:27017")
    private val users = client.getDatabase("rxtest").getCollection("users")
    private val products = client.getDatabase("rxtest").getCollection("products")

    fun addUser(name: String, currency: Currency): Observable<Success> {
        return users.insertOne(Document(mapOf("name" to name, "currency" to currency.name)))
    }

    fun getUser(name: String): Observable<User> {
        return users.find(Document(mapOf("name" to name))).toObservable().map(::User)
    }

    fun addProduct(productName: String, price: Double): Observable<Success> {
        return products.insertOne(Document(mapOf("productName" to productName, "price" to price)))
    }

    fun getProducts(): Observable<Product> {
        return products.find().toObservable().map(::Product)
    }
}
