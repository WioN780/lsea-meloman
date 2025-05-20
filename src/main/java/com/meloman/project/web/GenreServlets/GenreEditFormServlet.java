package com.meloman.project.web.GenreServlets;

import com.meloman.project.database_model.Genre;
import com.meloman.project.services.GenreServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/genres/edit")
public class GenreEditFormServlet extends HttpServlet {
    @EJB
    private GenreServiceBean genreService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        Genre g = genreService.getById(id);
        req.setAttribute("genre", g);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/genres/form.jsp");
        rd.forward(req, resp);
    }
}
