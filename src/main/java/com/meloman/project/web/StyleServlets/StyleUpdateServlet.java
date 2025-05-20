package com.meloman.project.web.StyleServlets;

import com.meloman.project.database_model.Style;
import com.meloman.project.services.StyleServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/styles/update")
public class StyleUpdateServlet extends HttpServlet {
    @EJB
    private StyleServiceBean StyleService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id   = req.getParameter("id");
        String name = req.getParameter("name");

        if (name == null || name.isBlank()) {
            Style tmp = new Style();
            tmp.setId(id);
            req.setAttribute("error", "Name must not be empty");
            req.setAttribute("Style", tmp);
            req.getRequestDispatcher("/WEB-INF/views/styles/form.jsp")
                    .forward(req, resp);
            return;
        }

        Style updated = new Style();
        updated.setId(id);
        updated.setName(name);
        StyleService.update(updated);
        resp.sendRedirect(req.getContextPath() + "/styles");
    }
}
