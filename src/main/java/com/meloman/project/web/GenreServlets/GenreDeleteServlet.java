package com.meloman.project.web.GenreServlets;

import com.meloman.project.services.GenreServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/genres/delete")
public class GenreDeleteServlet extends HttpServlet {
    @EJB
    private GenreServiceBean genreService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        genreService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/genres");
    }
}
