package com.meloman.project.web.ArtistServlets;

import com.meloman.project.database_model.Artist;
import com.meloman.project.services.ArtistServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/artists")
public class ArtistListServlet extends HttpServlet {
    @EJB
    private ArtistServiceBean artistService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Artist> artists = artistService.getAll();
        req.setAttribute("artists", artists);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/artists/list.jsp");
        rd.forward(req, resp);
    }
}
