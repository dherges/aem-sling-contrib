package de.spektrakel.sling.handlebarsjs;

import com.github.jknack.handlebars.io.URLTemplateLoader;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.io.IOException;
import java.net.URL;

/**
 * Handlebars template loader that reads templates from Sling repository
 */
public class SlingTemplateLoader extends URLTemplateLoader {

    protected final ResourceResolver resolver;

    public SlingTemplateLoader(ResourceResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected URL getResource(String location) throws IOException {
        final Resource resource = resolver.getResource(location);

        return (resource != null) ? resource.adaptTo(URL.class) : null;
    }
}
