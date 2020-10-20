package ru.akirakozov.sd.refactoring.servlet;

import kotlin.Pair;
import ru.akirakozov.sd.refactoring.HtmlWriter;
import ru.akirakozov.sd.refactoring.SqlFacade;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        if ("max".equals(command)) {
            var products = readAll("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

            htmlWriter.start();
            htmlWriter.h1("Product with max price: ");
            for (Pair<String, Integer> product : products) {
                htmlWriter.print(product.getFirst() + "\t" + product.getSecond());
                htmlWriter.br();
            }
            htmlWriter.end();
        } else if ("min".equals(command)) {
            var products = readAll("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

            htmlWriter.start();
            htmlWriter.h1("Product with min price: ");

            for (Pair<String, Integer> product : products) {
                htmlWriter.print(product.getFirst() + "\t" + product.getSecond());
                htmlWriter.br();
            }
            htmlWriter.end();
        } else if ("sum".equals(command)) {
            var sum = readInt("SELECT SUM(price) FROM PRODUCT");

            htmlWriter.start();
            htmlWriter.println("Summary price: ");
            if (sum != null) htmlWriter.println(sum);
            htmlWriter.end();
        } else if ("count".equals(command)) {
            var count = readInt("SELECT COUNT(*) FROM PRODUCT");

            htmlWriter.start();
            htmlWriter.println("Number of products: ");
            if (count != null) htmlWriter.println(count);
            htmlWriter.end();
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
