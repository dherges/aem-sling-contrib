/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.contrib.elasticsearch;


import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifyingResourceProvider;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceProvider;
import org.apache.sling.api.resource.ResourceResolver;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.get.GetField;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Service
@Properties({
        @Property(name = ResourceProvider.ROOTS, value = ElasticsearchResourceProvider.ROOT)
})
public class ElasticsearchResourceProvider implements ResourceProvider, ModifyingResourceProvider {

    public static final String ROOT = "/etc/elasticsearch";
    public static final String ABS_ROOT = "/" + ROOT;

    @Override
    public Resource getResource(ResourceResolver resourceResolver, HttpServletRequest httpServletRequest, String path) {
        return getResource(resourceResolver, path);
    }

    @Override
    public Resource getResource(ResourceResolver resourceResolver, String path) {
        try {
            apiStuff();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterator<Resource> listChildren(Resource resource) {
        return null;
    }

    @Override
    public Resource create(ResourceResolver resourceResolver, String s, Map<String, Object> stringObjectMap) throws PersistenceException {
        return null;
    }

    @Override
    public void delete(ResourceResolver resourceResolver, String s) throws PersistenceException {

    }

    @Override
    public void revert(ResourceResolver resourceResolver) {

    }

    @Override
    public void commit(ResourceResolver resourceResolver) throws PersistenceException {

    }

    @Override
    public boolean hasChanges(ResourceResolver resourceResolver) {
        return false;
    }


    private void stuff() {
        try {
            URLConnection httpURLConnection = new URL("http://Localhost:9200/example/cars/_search/?pretty").openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            String jsonStuff = IOUtils.toString(inputStream);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void apiStuff() throws ExecutionException, InterruptedException {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("Localhost", 9200));

        GetResponse response = client.prepareGet("example", "cars", "1")
                .execute()
                .get();

        Map<String, GetField> result = response.getFields();

        client.close();
    }


}
