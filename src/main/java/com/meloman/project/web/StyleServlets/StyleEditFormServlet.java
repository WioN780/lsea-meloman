package com.meloman.project.web.StyleServlets;

import com.meloman.project.database_model.Style;
import com.meloman.project.services.StyleServiceBean;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/styles/edit")
public class StyleEditFormServlet extends HttpServlet {
    @EJB
    private StyleServiceBean StyleService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        Style g = StyleService.getById(id);
        req.setAttribute("style", g);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/styles/form.jsp");
        rd.forward(req, resp);
    }
}
