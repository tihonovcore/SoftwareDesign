package api

import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Point2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.exitProcess

typealias DrawAction = (Graphics2D) -> Unit

class AwtDrawingApi : DrawingApi {
    private val actions = mutableListOf<DrawAction>()

    override fun drawCircle(center: Point2D, radius: Double) {
        actions += { g: Graphics2D ->
            g.paint = Color.green
            g.fill(Ellipse2D.Double(center.x - radius, center.y - radius, 2 * radius, 2 * radius))
        }
    }

    override fun drawArrow(from: Point, to: Point) {
        actions += { g: Graphics2D ->
            g.paint = Color.black
            g.drawLine(from.x, from.y, to.x, to.y)

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

            g.drawLine(to.x + x1.toInt(), to.y + y1.toInt(), to.x, to.y)
            g.drawLine(to.x + x2.toInt(), to.y + y2.toInt(), to.x, to.y)
        }
    }

    override fun render() {
        val frame = object : Frame() {
            override fun paint(g: Graphics) {
                actions.forEach { it(g as Graphics2D) }
            }
        }
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                exitProcess(0)
            }
        })
        frame.setSize(600, 400)
        frame.isVisible = true
    }
}
