package com.meloman.project.web.AlbumServlets;

import com.meloman.project.database_model.Album;
import com.meloman.project.database_model.Genre;
import com.meloman.project.database_model.Style;
import com.meloman.project.services.AlbumServiceBean;
import com.meloman.project.services.ArtistServiceBean;
import com.meloman.project.services.GenreServiceBean;
import com.meloman.project.services.StyleServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/albums/create")
public class AlbumCreateServlet extends HttpServlet {
    @EJB private AlbumServiceBean albumService;
    @EJB private ArtistServiceBean artistService;
    @EJB private GenreServiceBean genreService;
    @EJB private StyleServiceBean styleService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Album a = new Album();
        a.setTitle(req.getParameter("title"));
        a.setUrl(req.getParameter("url"));
        a.setContactInfo(req.getParameter("contactInfo"));

        try {
            a.setYear(Integer.parseInt(req.getParameter("year")));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Year must be a number");
            req.setAttribute("album", a);
            req.setAttribute("artists",   artistService.getAll());
            req.setAttribute("allGenres", genreService.getAll());
            req.setAttribute("allStyles", styleService.getAll());
            req.getRequestDispatcher("/WEB-INF/views/albums/form.jsp")
                    .forward(req, resp);
            return;
        }

        String artistId = req.getParameter("artistId");
        if (artistId != null && !artistId.isEmpty()) {
            a.setArtist(artistService.getById(artistId));
        }

        String[] genreIds = req.getParameterValues("genreIds");
        Set<Genre> genres = genreIds == null
                ? Collections.emptySet()
                : Arrays.stream(genreIds)
                .map(genreService::getById)
                .collect(Collectors.toSet());
        a.setGenres(genres);

        String[] styleIds = req.getParameterValues("styleIds");
        Set<Style> styles = styleIds == null
                ? Collections.emptySet()
                : Arrays.stream(styleIds)
                .map(styleService::getById)
                .collect(Collectors.toSet());
        a.setStyles(styles);

        albumService.create(a);
        resp.sendRedirect(req.getContextPath() + "/albums");
    }
}
