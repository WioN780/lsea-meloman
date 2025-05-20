package com.meloman.project.web.GenreServlets;

import com.meloman.project.database_model.Genre;
import com.meloman.project.services.GenreServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/genres/update")
public class GenreUpdateServlet extends HttpServlet {
    @EJB
    private GenreServiceBean genreService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id   = req.getParameter("id");
        String name = req.getParameter("name");

        if (name == null || name.isBlank()) {
            Genre tmp = new Genre();
            tmp.setId(id);
            req.setAttribute("error", "Name must not be empty");
            req.setAttribute("genre", tmp);
            req.getRequestDispatcher("/WEB-INF/views/genres/form.jsp")
                    .forward(req, resp);
            return;
        }

        Genre updated = new Genre();
        updated.setId(id);
        updated.setName(name);
        genreService.update(updated);
        resp.sendRedirect(req.getContextPath() + "/genres");
    }
}
