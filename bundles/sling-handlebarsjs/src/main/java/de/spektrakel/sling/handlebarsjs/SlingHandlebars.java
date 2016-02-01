/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.handlebarsjs;

import com.github.jknack.handlebars.Handlebars;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Handlebars engine that loads templates from Sling's content repository
 */
public class SlingHandlebars extends Handlebars {

    public SlingHandlebars(ResourceResolver resolver) {
        super(new SlingTemplateLoader(resolver));
    }

}
