import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Paths

internal object Config {
    private val path = System.getProperty("user.dir") + "/src/main/resources/private_config"
    private val properties = Files.readAllLines(Paths.get(path))

    fun loadToken(): String {
        for (property in properties) {
            val (key, value) = property.split(":")
            if (key == "token") {
                return value
            }
        }

        throw IllegalStateException("token not found")
    }
}
