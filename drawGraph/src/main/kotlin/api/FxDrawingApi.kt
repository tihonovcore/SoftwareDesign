package api

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FxDrawingApi : DrawingApi {
    companion object {
        private val actions = mutableListOf<(GraphicsContext) -> Unit>()
    }

    override fun drawCircle(center: Point2D, radius: Double) {
        actions += { gc ->
            gc.fill = Color.GREEN
            gc.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius)
        }
    }

    override fun drawArrow(from: Point, to: Point, margin: Double) {
        applyMargin(from, to, margin)
        applyMargin(to, from, margin)

        actions += { gc ->
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

    override fun render() {
        class Visualizer : Application() {
            override fun start(primaryStage: Stage) {
                val root = Group()
                val canvas = Canvas(600.0, 400.0)
                val gc: GraphicsContext = canvas.graphicsContext2D

                actions.forEach { it(gc) }

                root.children.add(canvas)
                primaryStage.scene = Scene(root)
                primaryStage.show()
            }
        }

        Application.launch(Visualizer::class.java)
    }
}
