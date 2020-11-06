package api

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FxDrawingApi(
    private val gc: GraphicsContext
) : DrawingApi {
    override fun drawCircle(center: Point2D, radius: Double) {
        gc.fill = Color.GREEN
        gc.fillOval(center.x, center.y, 2 * radius, 2 * radius)
    }

    override fun drawArrow(from: Point, to: Point) {
        gc.fill = Color.RED
        gc.strokeLine(from.x.toDouble(), from.y.toDouble(), to.x.toDouble(), to.y.toDouble())

        val length = from.distance(to)
        val newLength = 10
        val x = (from.x - to.x) / length * newLength
        val y = (from.y - to.y) / length * newLength

        val cosValue = cos(PI / 4)
        val sinValue = sin(PI / 4)

        val x1 = x * cosValue - y * sinValue
        val y1 = x * sinValue + y * cosValue

        val x2 = x * cosValue + y * sinValue
        val y2 = -x * sinValue + y * cosValue


        gc.strokeLine(to.x + x1, to.y + y1, to.x.toDouble(), to.y.toDouble())
        gc.strokeLine(to.x + x2, to.y + y2, to.x.toDouble(), to.y.toDouble())
    }
}
