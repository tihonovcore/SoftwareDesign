import junit.framework.TestCase
import javax.servlet.http.HttpServlet

abstract class AbstractServletTest : TestCase() {
    abstract val servletElements: List<Pair<HttpServlet, String>>
}
