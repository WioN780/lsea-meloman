package com.meloman.project.web.AlbumServlets;

import com.meloman.project.database_model.Album;
import com.meloman.project.services.AlbumServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/albums/edit")
public class AlbumEditFormServlet extends HttpServlet {
    @EJB
    private AlbumServiceBean albumService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        Album a = albumService.getById(id);
        req.setAttribute("album", a);
        req.getRequestDispatcher("/WEB-INF/views/albums/form.jsp")
                .forward(req, resp);
    }
}
