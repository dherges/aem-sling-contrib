package foo.sling;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletRequest;

import javax.servlet.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;


@SlingFilter(
        order = Integer.MAX_VALUE,
        scope = SlingFilterScope.REQUEST
)
public class ApiRequestFilter implements Filter {

    @Reference
    ApiResourceProvider apiResourceProvider;

    private final Target target = new Target();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) servletRequest;

        if (apiResourceProvider.valid(slingRequest.getResource().getPath())) {
            Class<? extends Annotation > verbAnnotation = target.of(slingRequest);
            Object t = target.adapt(slingRequest);
            Object value = null;
            try {
                value = target.call(t, verbAnnotation);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            servletRequest.setAttribute("value", value);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
