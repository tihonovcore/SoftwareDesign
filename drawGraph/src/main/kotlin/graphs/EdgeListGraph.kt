package graphs

import api.AwtDrawingApi
import api.DrawingApi
import api.FxDrawingApi
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos

data class Edge(
    val from: Int,
    val to: Int,
)

class EdgeListGraph(
    drawingApi: DrawingApi,
    val edges: List<Edge>
) : Graph(drawingApi) {
    private val countVertexes by lazy {
        val from = edges.map { it.from }.toSet()
        val to = edges.map { it.to }.toSet()
        (from + to).size
    }

    override fun draw() {
        val distanceBetweenVertexes = 2 * PI / countVertexes

        val x0 = 300.0
        val y0 = 225.0

        val centralX = 0.0
        val centralY = 170.0

        val vertexPosition = mutableListOf<Point2D>()
        repeat(countVertexes) { i ->
            val position = Point2D.Double(centralX, centralY).apply {
                shift(distanceBetweenVertexes * i)
                reflect()
                move(x0, y0)
            }
            vertexPosition += position
        }

        vertexPosition.forEach { pos ->
            drawingApi.drawCircle(pos, 10.0)
        }

        edges.forEach { edge ->
            val from = vertexPosition[edge.from].toPoint()
            val to = vertexPosition[edge.to].toPoint()
            drawingApi.drawArrow(from, to, 10.0)
        }

        drawingApi.render()
    }

    private fun Point2D.shift(radian: Double) {
        val sinValue = sin(radian)
        val cosValue = cos(radian)

        val newX = cosValue * x - sinValue * y
        val newY = sinValue * x + cosValue * y

        setLocation(newX, newY)
    }

    private fun Point2D.reflect() = setLocation(x, -y)

    private fun Point2D.move(dx: Double, dy: Double) {
        setLocation(x + dx, y + dy)
    }

    private fun Point2D.toPoint(): Point {
        return Point(x.toInt(), y.toInt())
    }
}

fun main() {
    val matrix = listOf(
        listOf(0, 1, 1, 1, 1),
        listOf(0, 0, 1, 1, 1),
        listOf(0, 0, 0, 1, 1),
        listOf(0, 0, 0, 0, 1),
        listOf(0, 0, 0, 0, 0),
    )

    val edges = listOf(
        Edge(0, 1),
        Edge(0, 2),
        Edge(0, 3),
        Edge(0, 4),
        Edge(1, 2),
        Edge(1, 3),
        Edge(1, 4),
        Edge(2, 3),
        Edge(2, 4),
        Edge(3, 4),
//        Edge(5, 6),
    )

//    MatrixGraph(AwtDrawingApi(), matrix).draw()
//    MatrixGraph(FxDrawingApi(), matrix).draw()

    EdgeListGraph(AwtDrawingApi(), edges).draw()
    EdgeListGraph(FxDrawingApi(), edges).draw()
}
