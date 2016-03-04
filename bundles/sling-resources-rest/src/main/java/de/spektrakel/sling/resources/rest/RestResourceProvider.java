/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.resources.rest;

import de.spektrakel.routing.sinatra.Route;
import de.spektrakel.routing.sinatra.Router;
import okhttp3.ResponseBody;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Service
@Properties({
        @Property(
                name = ResourceProvider.ROOTS,
                value = RestResourceProvider.ROOTS_DEFAULT),
        @Property(
                name = RestResourceProvider.BACKEND_BASE_URL,
                value = RestResourceProvider.BACKEND_BASE_URL_DEFAULT),
        @Property(
                name = RestResourceProvider.URL_PATTERN,
                value = RestResourceProvider.URL_PATTERN_DEFAULT)
})
public class RestResourceProvider implements ResourceProvider, ModifyingResourceProvider, QueriableResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(RestResourceProvider.class);

    public static final String ROOTS_DEFAULT = "/sling/resources/rest";
    public static final String BACKEND_BASE_URL = "restResourceProvider.backendBaseUrl";
    public static final String BACKEND_BASE_URL_DEFAULT = "http://192.168.0.214:4503";
    public static final String URL_PATTERN = "restResourceProvider.urlPattern";
    public static final String URL_PATTERN_DEFAULT = "/:id";

    protected String rootPath;
    protected String backendBaseUrl;
    protected String urlPattern;

    private Router router;
    private RemoteService remoteService;

    @Activate
    protected void activate(Map<String, Object> props) {
        rootPath = PropertiesUtil.toString(
                props.get(ResourceProvider.ROOTS),
                RestResourceProvider.ROOTS_DEFAULT);
        backendBaseUrl = PropertiesUtil.toString(
                props.get(RestResourceProvider.BACKEND_BASE_URL),
                RestResourceProvider.BACKEND_BASE_URL_DEFAULT);
        urlPattern = PropertiesUtil.toString(
                props.get(RestResourceProvider.URL_PATTERN),
                RestResourceProvider.URL_PATTERN_DEFAULT);

        router = new Router.Builder()
                .basePath(rootPath)
                .addRoute(new Route(urlPattern))
                .build();

        remoteService = new Retrofit.Builder()
                .baseUrl(backendBaseUrl)
                .build()
                .create(RemoteService.class);
    }


    @Override
    public Resource create(ResourceResolver resolver, String path, Map<String, Object> properties) throws PersistenceException {
        throw new PersistenceException("Not yet implemented");
    }

    @Override
    public void delete(ResourceResolver resolver, String path) throws PersistenceException {
        throw new PersistenceException("Not yet implemented");
    }

    @Override
    public boolean hasChanges(ResourceResolver resolver) {
        return false;
    }

    @Override
    public void revert(ResourceResolver resolver) {

    }

    @Override
    public void commit(ResourceResolver resolver) throws PersistenceException {

    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, HttpServletRequest request, String path) {
        return getResource(resourceResolver, path);
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, String path) {

        final Route.Match match = router.findMatch(path);
        if (match.isNoMatch()) {
            return null; // no match, no resource
        }

        final String id = match.param(":id");

        String resultObj = null;
        try {
            // prepare API call
            final Call<ResponseBody> call = remoteService.read(id);

            // call the remote REST service
            final Response<ResponseBody> response = call.execute();

            // obtain result object from response
            final ResponseBody responseBody = response.body();
            if (responseBody != null) {
                resultObj = responseBody.string();
            }
        } catch (IOException e) {
            LOG.error("IOException during read", e);
        }

        return new GenericObjectResource<>(resourceResolver, path, "sling:remoteRestResource",
                new ValueMapDecorator(new HashMap<>()), resultObj);
    }

    @Override
    public Iterator<Resource> listChildren(Resource parent) {
        return null;
    }

    @Override
    public Iterator<Resource> findResources(ResourceResolver resolver, String query, String language) {
        return null;
    }

    @Override
    public Iterator<ValueMap> queryResources(ResourceResolver resolver, String query, String language) {
        return null;
    }

}
