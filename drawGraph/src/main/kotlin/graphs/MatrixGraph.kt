package graphs

import api.DrawingApi
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MatrixGraph(
    drawingApi: DrawingApi,
    val matrix: List<List<Int>>
) : Graph(drawingApi) {
    private val countVertexes = matrix.size

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
