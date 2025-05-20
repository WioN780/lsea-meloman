package com.meloman.project.web.GenreServlets;

import com.meloman.project.database_model.Genre;
import com.meloman.project.services.GenreServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/genres")
public class GenreListServlet extends HttpServlet {
    @EJB
    private GenreServiceBean genreService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Genre> genres = genreService.getAll();
        req.setAttribute("genres", genres);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/genres/list.jsp");
        rd.forward(req, resp);
    }
}
