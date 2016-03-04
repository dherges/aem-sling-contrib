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

    public static final String ROOTS_DEFAULT = "/sling/resources/rest";
    public static final String BACKEND_BASE_URL = "restResourceProvider.backendBaseUrl";
    public static final String BACKEND_BASE_URL_DEFAULT = "http://192.168.0.214:4503";
    public static final String URL_PATTERN = "restResourceProvider.urlPattern";
    public static final String URL_PATTERN_DEFAULT = "/:id";

    protected String rootPath;
    protected String backendBaseUrl;
    protected String urlPattern;

    private Router router;

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
    }


    @Override
    public Resource create(ResourceResolver resolver, String path, Map<String, Object> properties) throws PersistenceException {
        return null;
    }

    @Override
    public void delete(ResourceResolver resolver, String path) throws PersistenceException {

    }

    @Override
    public void revert(ResourceResolver resolver) {

    }

    @Override
    public void commit(ResourceResolver resolver) throws PersistenceException {

    }

    @Override
    public boolean hasChanges(ResourceResolver resolver) {
        return false;
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, HttpServletRequest request, String path) {
        return getResource(resourceResolver, path);
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, String path) {

        final Route.Match match = router.findMatch(path);

        final String id = match.param(":id");

        final RemoteService remoteService = new Retrofit.Builder()
                .baseUrl(backendBaseUrl)
                .build()
                .create(RemoteService.class);

        String resultObj = null;
        ResponseBody objectFromRemote = null;
        try {
            Call<ResponseBody> call = remoteService.read(id);

            Response<ResponseBody> response = call.execute();

            objectFromRemote = response.body();

            resultObj = objectFromRemote.string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new GenericObjectResource<>(resourceResolver, path, "sling:remoteRestResource",
                new ValueMapDecorator(new HashMap<>()), resultObj);

        /*
        String relativePath = path.substring("/sling/resources/rest".length());

        Request request = new Request.Builder()
                .url("http://192.168.0.214:4503" + relativePath + ".json")
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseBody = response.body().string();

            // TODO: responseBody to json ...

            // TODO: json to JsonResource

            int a = 8+2; // yeehaw ... got response from remote service

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;*/
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


    /** True, if */
    protected boolean accepts(String path) {
        return true;
    }

    /** '/sling/resources/rest/abc/xyz' -> '/abc/xyz */
    protected String stripRootPath(String path) {
        return path.startsWith(rootPath) ? path.substring(rootPath.length()) : rootPath;
    }

    /** '/abc' -> {id: 'abc'} */
    protected Map<String, String> extractPathParams(String path) {
        final Map<String, String> pathParams = new HashMap<>();

        // TODO ... find named placeholder from urlPattern

        // TODO ... matches path urlPattern?

        // TODO ... find matches for named placeholders

        pathParams.put("id", "abc");

        return pathParams;
    }

    protected String buildPathParams(Map<String, String> pathParams) {

        // TODO ... for each named placeholder from urlPattern
        // TODO ... replace placeholder with value from pathParams

        return "/" + pathParams.get("id");
    }


}
