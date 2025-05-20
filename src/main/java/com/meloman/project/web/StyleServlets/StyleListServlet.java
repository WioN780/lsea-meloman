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
import java.util.List;

@WebServlet("/styles")
public class StyleListServlet extends HttpServlet {
    @EJB
    private StyleServiceBean StyleService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Style> Styles = StyleService.getAll();
        req.setAttribute("Styles", Styles);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/styles/list.jsp");
        rd.forward(req, resp);
    }
}
