/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.square.okhttp;

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;

/**
 * Simple okhttp logger adapter that logs to slf4j
 */
public class Slf4jLogger implements HttpLoggingInterceptor.Logger {

    private final Logger logger;

    public Slf4jLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(String s) {
        logger.info(s);
    }

}
