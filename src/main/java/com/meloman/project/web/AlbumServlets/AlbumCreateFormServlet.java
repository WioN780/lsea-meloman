package com.meloman.project.web.AlbumServlets;

import com.meloman.project.database_model.Album;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/albums/new")
public class AlbumCreateFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("album", new Album());  // empty backing bean
        req.getRequestDispatcher("/WEB-INF/views/albums/form.jsp")
                .forward(req, resp);
    }
}