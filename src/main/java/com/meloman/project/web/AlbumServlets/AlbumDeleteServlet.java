package com.meloman.project.web.AlbumServlets;

import com.meloman.project.services.AlbumServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/albums/delete")
public class AlbumDeleteServlet extends HttpServlet {
    @EJB
    private AlbumServiceBean albumService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        albumService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/albums");
    }
}
