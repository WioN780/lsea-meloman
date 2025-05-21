package com.meloman.project.web.StyleServlets;

import com.meloman.project.services.StyleServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/styles/delete")
public class StyleDeleteServlet extends HttpServlet {
    @EJB
    private StyleServiceBean StyleService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        StyleService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/styles");
    }
}
