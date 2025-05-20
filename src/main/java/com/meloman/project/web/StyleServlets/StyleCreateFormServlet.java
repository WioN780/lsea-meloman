package com.meloman.project.web.StyleServlets;

import com.meloman.project.database_model.Style;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/styles/new")
public class StyleCreateFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("Style", new Style());
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/styles/form.jsp");
        rd.forward(req, resp);
    }
}
