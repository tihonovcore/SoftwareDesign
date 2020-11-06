package api

import java.awt.Point
import java.awt.geom.Point2D

fun main() {
    val api = AwtDrawingApi()
    api.drawArrow(Point(100, 150), Point(200, 50))
    api.drawArrow(Point(200, 50), Point(500, 300))
    api.drawArrow(Point(500, 300), Point(500, 50))
    api.drawArrow(Point(500, 50), Point(300, 300))
    api.drawCircle(Point2D.Double(100.0, 100.0), 10.0)
    api.render()
}
