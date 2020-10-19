package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.HtmlWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var htmlWriter = new HtmlWriter(response.getWriter());

        try {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT");
                htmlWriter.start();

                while (resultSet.next()) {
                    String  name = resultSet.getString("name");
                    int price  = resultSet.getInt("price");

                    htmlWriter.print(name + "\t" + price);
                    htmlWriter.br();
                }
                htmlWriter.end();

                resultSet.close();
                statement.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
