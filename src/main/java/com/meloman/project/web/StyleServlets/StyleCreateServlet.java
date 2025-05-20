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


@WebServlet("/styles/create")
public class StyleCreateServlet extends HttpServlet {
    @EJB
    private StyleServiceBean StyleService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Name must not be empty");
            req.setAttribute("Style", new Style());
            req.getRequestDispatcher("/WEB-INF/views/styles/form.jsp")
                    .forward(req, resp);
            return;
        }

        Style g = new Style();
        g.setName(name);
        StyleService.create(g);
        resp.sendRedirect(req.getContextPath() + "/styles");
    }
}
