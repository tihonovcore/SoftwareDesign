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
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        if ("max".equals(command)) {
            new SqlFacade("jdbc:sqlite:test.db").databaseAction(statement -> {
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    htmlWriter.start();
                    htmlWriter.h1("Product with max price: ");

                    while (resultSet.next()) {
                        String  name = resultSet.getString("name");
                        int price  = resultSet.getInt("price");

                        htmlWriter.print(name + "\t" + price);
                        htmlWriter.br();
                    }
                    htmlWriter.end();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if ("min".equals(command)) {
            new SqlFacade("jdbc:sqlite:test.db").databaseAction(statement -> {
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    htmlWriter.start();
                    htmlWriter.h1("Product with min price: ");

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
        } else if ("sum".equals(command)) {
            new SqlFacade("jdbc:sqlite:test.db").databaseAction(statement -> {
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    htmlWriter.start();
                    htmlWriter.println("Summary price: ");

                    if (resultSet.next()) {
                        htmlWriter.println(resultSet.getInt(1));
                    }
                    htmlWriter.end();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if ("count".equals(command)) {
            new SqlFacade("jdbc:sqlite:test.db").databaseAction(statement -> {
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    htmlWriter.start();
                    htmlWriter.println("Number of products: ");

                    if (resultSet.next()) {
                        htmlWriter.println(resultSet.getInt(1));
                    }
                    htmlWriter.end();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
