package com.meloman.project.web.GenreServlets;

import com.meloman.project.database_model.Genre;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/genres/new")
public class GenreCreateFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("genre", new Genre());
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/genres/form.jsp");
        rd.forward(req, resp);
    }
}
