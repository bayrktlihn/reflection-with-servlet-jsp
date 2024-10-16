package io.bayrktlihn.reflectionwithservletjsp;

import io.bayrktlihn.reflectionwithservletjsp.enums.RequestMethod;
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
        handle(req, resp, RequestMethod.GET);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp, RequestMethod requestMethod) {

        ServletContext servletContext = getServletContext();

        List<RequestCommand> requestCommands = (List<RequestCommand>) servletContext.getAttribute("requestHandlerCommands");

        Optional<RequestCommand> requestHandlerCommandOptional = requestCommands.stream().filter(item -> item.getPath().equals(req.getPathInfo()) && requestMethod.equals(item.getRequestMethod())).findFirst();

        if(requestHandlerCommandOptional.isPresent()) {
            try {
                RequestCommand requestCommand = requestHandlerCommandOptional.get();
                Object controller = requestCommand.getController();
                Method method = requestCommand.getToBeInvokedMethod();
                method.invoke(controller, req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
