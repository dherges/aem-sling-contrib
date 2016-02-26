/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package spark.demo;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

import org.apache.felix.scr.annotations.*;
import spark.servlet.SparkFilter;

import javax.servlet.*;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;


@Component(
        label = "Sling Spark Filter",
        description = "Provides an entry point to spark applications in Apache Felix",
        metatype = true
)
public class SlingSparkFilter extends SparkFilter implements Filter {

    public static final String APP = DemoApplication.class.getName();

    private ServiceRegistration filterRegistration;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        super.doFilter(request, response, null); // pass 'null' as chain param to explicitly disable succeeding filters
    }

    @Activate
    protected final void activate(ComponentContext ctx) {
        Dictionary config = ctx.getProperties();

        Hashtable filterProps = new Hashtable();
        filterProps.put("pattern", "/spark.*");
        filterProps.put("init.filterMappingUrlPattern", "/spark/*");
        filterProps.put("init." + SparkFilter.APPLICATION_CLASS_PARAM, APP);
        this.filterRegistration = ctx.getBundleContext().registerService(Filter.class.getName(), this, filterProps);
    }

    @Deactivate
    protected final void deactivate() {
        if (this.filterRegistration != null) {
            this.filterRegistration.unregister();
            this.filterRegistration = null;
        }
    }
}
