package com.meloman.project.web.ArtistServlets;

import com.meloman.project.database_model.Artist;
import com.meloman.project.services.ArtistServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/artists/update")
public class ArtistUpdateServlet extends HttpServlet {
    @EJB
    private ArtistServiceBean artistService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id          = req.getParameter("id");
        String name        = req.getParameter("name");
        String realName    = req.getParameter("realName");
        String contactInfo = req.getParameter("contactInfo");
        String url         = req.getParameter("url");

        if (name == null || name.isBlank()) {
            Artist tmp = new Artist();
            tmp.setId(id);
            req.setAttribute("error", "Name is required");
            req.setAttribute("artist", tmp);
            req.getRequestDispatcher("/WEB-INF/views/artists/form.jsp")
                    .forward(req, resp);
            return;
        }

        Artist updated = new Artist();
        updated.setId(id);
        updated.setName(name);
        updated.setRealName(realName);
        updated.setContactInfo(contactInfo);
        updated.setUrl(url);

        artistService.update(updated);
        resp.sendRedirect(req.getContextPath() + "/artists");
    }
}
