package api

import java.awt.Point
import java.awt.geom.Point2D

interface DrawingApi {
    val width: Double
    val height: Double

    fun drawCircle(center: Point2D, radius: Double)
    fun drawArrow(from: Point, to: Point, margin: Double = 0.0)

    fun render()
}
