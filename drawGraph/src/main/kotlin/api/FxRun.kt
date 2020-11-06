package api

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.stage.Stage
import java.awt.Point
import java.awt.geom.Point2D

class JavaFxDrawCircle : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Drawing circle"
        val root = Group()
        val canvas = Canvas(600.0, 400.0)
        val gc: GraphicsContext = canvas.graphicsContext2D

        val api = FxDrawingApi(gc)

        //todo: is there better way
        list.forEach { it(api) }

        root.children.add(canvas)
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }
}

//NOTE: dirty hack
val list = mutableListOf<(FxDrawingApi) -> Unit>()

fun drawArrow(from: Point, to: Point) {
    list += { api: FxDrawingApi -> api.drawArrow(from, to) }
}

fun drawCircle(center: Point2D, radius: Double) {
    list += { api: FxDrawingApi -> api.drawCircle(center, radius) }
}

fun main(args: Array<String>) {
    drawArrow(Point(100, 150), Point(200, 50))
    drawArrow(Point(100, 150), Point(200, 50))
    drawArrow(Point(200, 50), Point(500, 300))
    drawArrow(Point(500, 300), Point(500, 50))
    drawArrow(Point(500, 50), Point(300, 300))
    drawCircle(Point2D.Double(100.0, 100.0), 10.0)

    Application.launch(JavaFxDrawCircle::class.java)
}
