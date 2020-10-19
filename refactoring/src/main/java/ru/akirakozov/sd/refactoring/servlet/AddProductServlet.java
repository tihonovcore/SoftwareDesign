package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.SqlFacade;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        new SqlFacade("jdbc:sqlite:test.db").databaseAction(statement -> {
            try {
                String sql = "INSERT INTO PRODUCT " + "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
