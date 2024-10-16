package io.bayrktlihn.reflectionwithservletjsp;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@WebServlet("/*")
public class DispathcherServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) {

        ServletContext servletContext = getServletContext();

        List<RequestHandlerCommand> requestHandlerCommands = (List<RequestHandlerCommand>) servletContext.getAttribute("requestHandlerCommands");

        Optional<RequestHandlerCommand> requestHandlerCommandOptional = requestHandlerCommands.stream().filter(item -> item.getPath().equals(req.getPathInfo())).findFirst();

        if(requestHandlerCommandOptional.isPresent()) {
            try {
                RequestHandlerCommand requestHandlerCommand = requestHandlerCommandOptional.get();
                Object controller = requestHandlerCommand.getController();
                Method method = requestHandlerCommand.getMethod();
                method.invoke(controller, req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
