package geometry

import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.cos
import kotlin.math.sin

internal fun applyMargin(from: Point, to: Point, margin: Double) {
    val length = from.distance(to)

    val normalizedX = (to.x - from.x) / length
    val normalizedY = (to.y - from.y) / length

    val xWithMargin = normalizedX * (length - margin)
    val yWithMargin = normalizedY * (length - margin)

    to.x = from.x + xWithMargin.toInt()
    to.y = from.y + yWithMargin.toInt()
}

internal fun Point2D.shift(radian: Double) {
    val sinValue = sin(radian)
    val cosValue = cos(radian)

    val newX = cosValue * x - sinValue * y
    val newY = sinValue * x + cosValue * y

    setLocation(newX, newY)
}

internal fun Point2D.reflect() = setLocation(x, -y)

internal fun Point2D.move(dx: Double, dy: Double) {
    setLocation(x + dx, y + dy)
}

internal fun Point2D.toPoint(): Point {
    return Point(x.toInt(), y.toInt())
}
