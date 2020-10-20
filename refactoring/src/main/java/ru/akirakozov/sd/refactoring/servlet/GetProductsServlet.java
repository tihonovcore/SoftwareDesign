package ru.akirakozov.sd.refactoring.servlet;

import kotlin.Pair;
import ru.akirakozov.sd.refactoring.HtmlWriter;
import ru.akirakozov.sd.refactoring.SqlFacade;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractProductServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var htmlWriter = new HtmlWriter(response.getWriter());

        var products = readAll("SELECT * FROM PRODUCT");

        htmlWriter.start();
        for (Pair<String, Integer> product : products) {
            htmlWriter.print(product.getFirst() + "\t" + product.getSecond());
            htmlWriter.br();
        }
        htmlWriter.end();

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
