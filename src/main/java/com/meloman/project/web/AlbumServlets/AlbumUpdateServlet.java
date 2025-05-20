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

@WebServlet("/albums/update")
public class AlbumUpdateServlet extends HttpServlet {
    @EJB
    private AlbumServiceBean albumService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Album a = new Album();
        a.setId(req.getParameter("id"));
        a.setTitle(req.getParameter("title"));
        a.setUrl(req.getParameter("url"));
        a.setContactInfo(req.getParameter("contactInfo"));
        try {
            a.setYear(Integer.parseInt(req.getParameter("year")));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Year must be a number");
            req.setAttribute("album", a);
            req.getRequestDispatcher("/WEB-INF/views/albums/form.jsp")
                    .forward(req, resp);
            return;
        }
        albumService.update(a);
        resp.sendRedirect(req.getContextPath() + "/albums");
    }
}
