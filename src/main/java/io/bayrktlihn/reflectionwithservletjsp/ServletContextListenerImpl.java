package io.bayrktlihn.reflectionwithservletjsp;

import io.bayrktlihn.reflectionwithservletjsp.annotations.RequestMapping;
import io.bayrktlihn.reflectionwithservletjsp.annotations.RestController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);

        ConfigurationBuilder configuration = new ConfigurationBuilder();
        configuration.setUrls(ClasspathHelper.forPackage("io.bayrktlihn.reflectionwithservletjsp"));
        configuration.addScanners(Scanners.SubTypes.filterResultsBy(s -> true), Scanners.TypesAnnotated,
                Scanners.MethodsAnnotated);

        Reflections reflections = new Reflections(configuration);

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(RestController.class);

        Set<Object> controllers = new HashSet<>();

        List<RequestCommand> requestCommands = new ArrayList<>();


        for (Class<?> clazz : typesAnnotatedWith) {
            if (!clazz.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            Object o = controllers.stream().filter(item -> item.getClass() == clazz).findFirst().orElse(null);
            o = addIfNull(clazz, o, controllers);

            RequestMapping controllerRequestMapping = clazz.getDeclaredAnnotation(RequestMapping.class);
            String baseUrl = controllerRequestMapping.path();

            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping methodRequestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    String fullPath = baseUrl + methodRequestMapping.path();

                    RequestCommand e = new RequestCommand();
                    e.setPath(fullPath);
                    e.setController(o);
                    e.setToBeInvokedMethod(method);
                    e.setRequestMethod(methodRequestMapping.method());
                    requestCommands.add(e);
                }
            }

        }


        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute("requestHandlerCommands", requestCommands);
    }

    private static Object addIfNull(Class<?> clazz, Object o, Set<Object> controllers) {
        if (o == null) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                o = constructor.newInstance();
                controllers.add(controllers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }
}
