/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.email;

import org.apache.sling.api.SlingIOException;
import org.apache.sling.api.resource.Resource;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A DataSource implementation that allows creating MIME message content from a Sling resource
 */
public class SlingResourceDataSource implements DataSource {

    protected final Resource resource;
    protected final String name;
    protected final String contentType;

    public SlingResourceDataSource(Resource resource, String name, String contentType) {
        this.resource = resource;
        this.name = name;
        this.contentType = contentType;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.adaptTo(InputStream.class);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException(new SlingIOException(new IOException("Not supported")));
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return name;
    }

}
