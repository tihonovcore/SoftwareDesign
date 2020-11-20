package graphs

import api.DrawingApi

abstract class Graph(
    protected val drawingApi: DrawingApi
) {
    abstract val countVertexes: Int
    abstract fun draw()
}
