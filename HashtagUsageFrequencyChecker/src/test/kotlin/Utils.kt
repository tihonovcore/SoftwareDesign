import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.mockito.Mockito
import java.nio.file.Files
import java.nio.file.Paths

internal fun readFiles(vararg names: String): List<String> {
    val result = mutableListOf<String>()
    for (name in names) {
        val path = System.getProperty("user.dir") + "/src/test/resources/$name"
        result += Files.readString(Paths.get(path))
    }
    return result
}

internal fun List<String>.toJsonVK(): List<JsonElement> {
    val result = mutableListOf<JsonElement>()
    for (json in this) {
        result += JsonParser.parseString(json)
    }

    return result
}

internal fun <T> any(): T = Mockito.any<T>()
internal fun <T> eq(obj: T): T = Mockito.eq<T>(obj)
