import api.AwtDrawingApi
import api.DrawingApi
import api.FxDrawingApi
import graphs.Edge
import graphs.EdgeListGraph
import graphs.Graph
import graphs.MatrixGraph
import java.lang.IllegalStateException

fun main(args: Array<String>) {
    val errorMessage =
        """Expected arguments: <drawing_api> <graph_type>
          |Where
          |    drawing_api is `fx`     or `awt`
          |    graph_type  is `matrix` or `edge`
          |But was: $args
        """.trimMargin()

    require(args.size == 2) { errorMessage }

    val api: DrawingApi = when(args[0]) {
        "fx" -> FxDrawingApi()
        "awt" -> AwtDrawingApi()
        else -> throw IllegalStateException(errorMessage)
    }

    val graph: Graph = when(args[1]) {
        "matrix" -> readMatrixGraph(api)
        "edge" -> readEdgeGraph(api)
        else -> throw IllegalStateException(errorMessage)
    }

    graph.draw()
}

private fun String.int(): Int {
    return Integer.parseInt(this)
}

private fun readMatrixGraph(api: DrawingApi): Graph {
    val n = readLine()!!.int()
    val matrix = mutableListOf<List<Int>>()
    repeat(n) {
        matrix += readLine()!!.split(" ").map { it.int() }
    }

    return MatrixGraph(api, matrix)
}

private fun readEdgeGraph(api: DrawingApi): Graph {
    val m = readLine()!!.int()
    val edges = mutableListOf<Edge>()
    repeat(m) {
        val (from, to) = readLine()!!.split(" ").map { it.int() }
        edges += Edge(from, to)
    }

    return EdgeListGraph(api, edges)
}
