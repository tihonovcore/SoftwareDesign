package ru.akirakozov.sd.refactoring.servlet;

import kotlin.Pair;
import ru.akirakozov.sd.refactoring.HtmlWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractProductServlet {
    private final List<String> commands = Arrays.asList("max", "min", "count", "sum");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        if (commands.contains(command)) {
            htmlWriter.start();

            switch (command) {
                case "max": {
                    var products = readAll("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");

                    htmlWriter.h1("Product with max price: ");
                    for (Pair<String, Integer> product : products) {
                        htmlWriter.print(product.getFirst() + "\t" + product.getSecond());
                        htmlWriter.br();
                    }

                    break;
                }
                case "min" : {
                    var products = readAll("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");

                    htmlWriter.h1("Product with min price: ");
                    for (Pair<String, Integer> product : products) {
                        htmlWriter.print(product.getFirst() + "\t" + product.getSecond());
                        htmlWriter.br();
                    }

                    break;
                }
                case "sum" : {
                    var sum = readInt("SELECT SUM(price) FROM PRODUCT");

                    htmlWriter.println("Summary price: ");
                    if (sum != null) htmlWriter.println(sum);

                    break;
                }
                case "count" : {
                    var count = readInt("SELECT COUNT(*) FROM PRODUCT");

                    htmlWriter.println("Number of products: ");
                    if (count != null) htmlWriter.println(count);

                    break;
                }
            }

            htmlWriter.end();
        } else {
            htmlWriter.println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
