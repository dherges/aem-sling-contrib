package foo.sling;

import foo.sling.api.*;
import org.apache.sling.api.SlingHttpServletRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Target { // <-- we need a name for this class


    /** calls the target object for the given verb annotation */
    public Object call(Object target, Class<? extends Annotation> annotationTarget)
            throws InvocationTargetException, IllegalAccessException {

        // Call to request handling method
        Class<?> classOfValue = null;
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(annotationTarget)) {
                classOfValue = m.getReturnType();

                return m.invoke(target);
            }
        }

        throw new RuntimeException("Method Not Allowed ... TODO 405 status code from exception");
    }

    /** adapts request.adaptTo(targetType) */
    public Object adapt(SlingHttpServletRequest request) {
        Class<?> targetType = (Class<?>) request.getResource().getResourceMetadata().get("targetType");

        return request.adaptTo(targetType);
    }

    /** determines annotation type based on request method */
    public Class<? extends Annotation> of(SlingHttpServletRequest request) {
        String method = request.getMethod();

        Class<? extends Annotation> annotationTarget = GET.class;
        if ("GET".equals(method)) {
            annotationTarget = GET.class;
        } else if ("POST".equals(method)) {
            annotationTarget = POST.class;
        } else if ("PUT".equals(method)) {
            annotationTarget = PUT.class;
        } else if ("DELETE".equals(method)) {
            annotationTarget = DELETE.class;
        } else if ("HEAD".equals(method)) {
            annotationTarget = HEAD.class;
        }

        return annotationTarget;
    }

}
