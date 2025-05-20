package com.meloman.project.web.AlbumServlets;

import com.meloman.project.database_model.Album;
import com.meloman.project.services.AlbumServiceBean;
import com.meloman.project.services.ArtistServiceBean;
import com.meloman.project.services.GenreServiceBean;
import com.meloman.project.services.StyleServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/albums/edit")
public class AlbumEditFormServlet extends HttpServlet {
    @EJB private AlbumServiceBean  albumService;
    @EJB private ArtistServiceBean artistService;
    @EJB private GenreServiceBean  genreService;
    @EJB private StyleServiceBean  styleService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        Album a = albumService.getById(id);
        req.setAttribute("album", a);

        // ← load dropdown & checkbox data exactly like in the CreateForm
        req.setAttribute("artists",   artistService.getAll());
        req.setAttribute("allGenres", genreService.getAll());
        req.setAttribute("allStyles", styleService.getAll());

        req.getRequestDispatcher("/WEB-INF/views/albums/form.jsp")
                .forward(req, resp);
    }
}
