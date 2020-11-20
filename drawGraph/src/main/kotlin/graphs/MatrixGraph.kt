package graphs

import api.DrawingApi
import java.awt.geom.Point2D
import kotlin.math.PI

import geometry.*

class MatrixGraph(
    drawingApi: DrawingApi,
    private val matrix: List<List<Int>>
) : Graph(drawingApi) {
    override val countVertexes = matrix.size

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

        matrix.forEachIndexed { i, line ->
            line.forEachIndexed { j, exists ->
                if (exists == 1) {
                    val from = vertexPosition[i].toPoint()
                    val to = vertexPosition[j].toPoint()
                    drawingApi.drawArrow(from, to, 10.0)
                }
            }
        }

        drawingApi.render()
    }
}
