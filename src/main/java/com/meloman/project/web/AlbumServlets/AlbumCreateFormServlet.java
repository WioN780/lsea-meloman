package com.meloman.project.web.AlbumServlets;

import com.meloman.project.database_model.Album;
import com.meloman.project.services.ArtistServiceBean;
import com.meloman.project.services.GenreServiceBean;
import com.meloman.project.services.StyleServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/albums/new")
public class AlbumCreateFormServlet extends HttpServlet {
    @EJB private ArtistServiceBean artistService;
    @EJB private GenreServiceBean  genreService;
    @EJB private StyleServiceBean  styleService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("album", new Album());

        req.setAttribute("artists",   artistService.getAll());
        req.setAttribute("allGenres", genreService.getAll());
        req.setAttribute("allStyles", styleService.getAll());

        req.getRequestDispatcher("/WEB-INF/views/albums/form.jsp")
                .forward(req, resp);
    }
}
