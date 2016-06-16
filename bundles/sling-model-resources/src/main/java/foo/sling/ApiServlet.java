package foo.sling;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Rfc3339DateJsonAdapter;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


@SlingServlet(
        resourceTypes = { "procato-workbench/api" }, // TODO <-- this could also be 'sling/servlet/default' combined w/ OptingServlet (request) -> request.resource.resourceMetaData.has('adaptTarget')
        methods = { "GET", "POST", "PUT", "DELETE", "HEAD" },
        extensions = { "json" } // TODO <-- this could also handle 'xml', 'protobuf'
)
public class ApiServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ApiServlet.class);

    private final Target target = new Target();


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        handle(request, response);
    }


    private void handle(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        try {
            Object value = request.getAttribute("value");

            // TODO: this could also happen with an indirection for serialization
            Moshi moshi = new Moshi.Builder().add(Date.class, new Rfc3339DateJsonAdapter()).build();
            JsonAdapter<Object> jsonAdapter = moshi.adapter(Object.class);

            response.setContentType("application/json");
            writer.append(jsonAdapter.toJson(value));

        } catch (RuntimeException e) {

            // TODO ... exception mapping for status codes
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            writer.append(e.getMessage());
        }
    }

}
