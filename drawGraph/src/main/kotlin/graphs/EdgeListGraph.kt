package graphs

import api.AwtDrawingApi
import api.DrawingApi
import api.FxDrawingApi
import geometry.*
import java.awt.geom.Point2D
import kotlin.math.PI

data class Edge(
    val from: Int,
    val to: Int,
)

class EdgeListGraph(
    drawingApi: DrawingApi,
    private val edges: List<Edge>
) : Graph(drawingApi) {
    override val countVertexes by lazy {
        val from = edges.map { it.from }.toSet()
        val to = edges.map { it.to }.toSet()
        (from + to).size
    }

    override fun draw() {
        val distanceBetweenVertexes = 2 * PI / countVertexes

        val x0 = drawingApi.width / 2
        val y0 = drawingApi.height / 2

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
}
