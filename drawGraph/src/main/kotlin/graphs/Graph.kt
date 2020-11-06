package graphs

import api.DrawingApi

abstract class Graph(
    private val drawingApi: DrawingApi
) {
    abstract fun draw()
}
