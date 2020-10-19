package ru.akirakozov.sd.refactoring

import java.io.PrintWriter

class HtmlWriter (
    private val writer: PrintWriter
) {
    private val open = "<html><body>"
    private val close = "</body></html>"

    fun start() = writer.println(open)

    fun end() = writer.println(close)

    fun <T> print(t: T) = writer.print(t)

    fun <T> println(t: T) = writer.println("$t")

    fun br() = writer.println("</br>")

    fun h1(s: String) = writer.println("<h1>$s</h1>")
}
