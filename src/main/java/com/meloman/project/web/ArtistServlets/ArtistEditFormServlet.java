package com.meloman.project.web.ArtistServlets;

import com.meloman.project.database_model.Artist;
import com.meloman.project.services.ArtistServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/artists/edit")
public class ArtistEditFormServlet extends HttpServlet {
    @EJB
    private ArtistServiceBean artistService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        Artist a = artistService.getById(id);
        req.setAttribute("artist", a);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/artists/form.jsp");
        rd.forward(req, resp);
    }
}
