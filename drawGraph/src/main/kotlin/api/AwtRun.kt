package api

import java.awt.Point
import java.awt.geom.Point2D

fun main() {
    with(AwtDrawingApi()) {
        drawArrow(Point(100, 150), Point(200, 50))
        drawArrow(Point(200, 50), Point(500, 300))
        drawArrow(Point(500, 300), Point(500, 50))
        drawArrow(Point(500, 50), Point(300, 300))
        drawCircle(Point2D.Double(100.0, 100.0), 10.0)
        render()
    }
}
