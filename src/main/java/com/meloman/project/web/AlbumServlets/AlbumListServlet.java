package com.meloman.project.web.AlbumServlets;

import com.meloman.project.database_model.Album;
import com.meloman.project.services.AlbumServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/albums")
public class AlbumListServlet extends HttpServlet {

    @EJB
    private AlbumServiceBean albumService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Album> albums = albumService.getAll();

        req.setAttribute("albums", albums);

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/albums/list.jsp");
        rd.forward(req, resp);
    }
}
