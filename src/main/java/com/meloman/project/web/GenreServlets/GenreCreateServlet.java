package com.meloman.project.web.GenreServlets;

import com.meloman.project.database_model.Genre;
import com.meloman.project.services.GenreServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/genres/create")
public class GenreCreateServlet extends HttpServlet {
    @EJB
    private GenreServiceBean genreService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Name must not be empty");
            req.setAttribute("genre", new Genre());
            req.getRequestDispatcher("/WEB-INF/views/genres/form.jsp")
                    .forward(req, resp);
            return;
        }

        Genre g = new Genre();
        g.setName(name);
        genreService.create(g);
        resp.sendRedirect(req.getContextPath() + "/genres");
    }
}
