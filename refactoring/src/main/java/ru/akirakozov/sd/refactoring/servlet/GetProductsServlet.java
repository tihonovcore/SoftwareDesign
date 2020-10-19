package ru.akirakozov.sd.refactoring.servlet;

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
public class GetProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var htmlWriter = new HtmlWriter(response.getWriter());

        new SqlFacade("jdbc:sqlite:test.db").databaseAction(statement -> {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT");
                htmlWriter.start();

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");

                    htmlWriter.print(name + "\t" + price);
                    htmlWriter.br();
                }
                htmlWriter.end();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
