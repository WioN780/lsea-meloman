package com.meloman.project.web.ArtistServlets;

import com.meloman.project.database_model.Artist;
import com.meloman.project.services.ArtistServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/artists/create")
public class ArtistCreateServlet extends HttpServlet {
    @EJB
    private ArtistServiceBean artistService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name        = req.getParameter("name");
        String realName    = req.getParameter("realName");
        String contactInfo = req.getParameter("contactInfo");
        String url         = req.getParameter("url");

        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Name is required");
            req.setAttribute("artist", new Artist());
            req.getRequestDispatcher("/WEB-INF/views/artists/form.jsp")
                    .forward(req, resp);
            return;
        }

        Artist a = new Artist();
        a.setName(name);
        a.setRealName(realName);
        a.setContactInfo(contactInfo);
        a.setUrl(url);

        artistService.create(a);
        resp.sendRedirect(req.getContextPath() + "/artists");
    }
}
