package foo.products;

import foo.sling.api.GET;
import foo.sling.api.Path;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;
import java.util.List;


@Path("/api/product")
@Model(adaptables = SlingHttpServletRequest.class)
public class Product {

    @Inject @Self
    SlingHttpServletRequest request;

    @Inject @Source("request-parameters-string") @Optional @Default(values = "")
    String sku;

    @GET
    public Object get() {

        return null;
    }

}
