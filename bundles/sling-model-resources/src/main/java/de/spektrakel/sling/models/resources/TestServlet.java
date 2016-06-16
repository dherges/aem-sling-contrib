/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.models.resources;

import de.spektrakel.sling.models.resources.demo.TestModel;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import java.io.IOException;

/*
@Component(metatype = true, immediate = true, policy = ConfigurationPolicy.OPTIONAL)
@Service(Servlet.class)
@Properties({
        @Property(
                name = "sling.servlet.paths",
                value = "/services/sling/models",
                propertyPrivate = true
        )
})*/

@SlingServlet(resourceTypes = "sling/model/resource")
public class TestServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        TestModel testModel = request.getResource().adaptTo(TestModel.class);


        testModel.postToElastic();


        int a = 8+2;
    }

}
