/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package vue;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import java.io.*;
import java.net.URL;

@SlingServlet(
        resourceTypes = {"vue/component"},
        methods = {"GET"},
        extensions = "html"
)
public class VueComponentServlet extends SlingSafeMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        final String resourceType = request.getResource().getResourceType();

        // TODO ... better do this
        final String hbsPath = "/apps/" + resourceType + "/" + ResourceUtil.getName(resourceType);

        final URL fileContent = request.getResourceResolver().getResource(hbsPath + ".hbs").adaptTo(URL.class);


        // Content-Type header
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        // Output rendered html thing...
        // hbsTemplate.apply(new Object(), response.getWriter());

        Writer writer = response.getWriter();
        BufferedReader in = new BufferedReader(new InputStreamReader(fileContent.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            writer.append(inputLine);
        in.close();
    }

}
