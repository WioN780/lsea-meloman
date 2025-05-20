package com.meloman.project.web.ArtistServlets;

import com.meloman.project.services.ArtistServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/artists/delete")
public class ArtistDeleteServlet extends HttpServlet {
    @EJB
    private ArtistServiceBean artistService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        artistService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/artists");
    }
}
