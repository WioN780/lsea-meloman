package com.meloman.project.web.ArtistServlets;

import com.meloman.project.database_model.Artist;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/artists/new")
public class ArtistCreateFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("artist", new Artist());
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/artists/form.jsp");
        rd.forward(req, resp);
    }
}
