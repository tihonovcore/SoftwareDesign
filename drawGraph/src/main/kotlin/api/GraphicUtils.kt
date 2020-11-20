package api

import java.awt.Point

internal fun applyMargin(from: Point, to: Point, margin: Double) {
    val length = from.distance(to)

    val normalizedX = (to.x - from.x) / length
    val normalizedY = (to.y - from.y) / length

    val xWithMargin = normalizedX * (length - margin)
    val yWithMargin = normalizedY * (length - margin)

    to.x = from.x + xWithMargin.toInt()
    to.y = from.y + yWithMargin.toInt()
}
