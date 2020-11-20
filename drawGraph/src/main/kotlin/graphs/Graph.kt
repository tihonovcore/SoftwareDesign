package graphs

import api.DrawingApi

abstract class Graph(
    protected val drawingApi: DrawingApi
) {
    abstract fun draw()
}
