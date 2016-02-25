/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package spark.demo;

import spark.servlet.SparkApplication;

import static spark.Spark.get;

public class DemoApplication implements SparkApplication {

    @Override
    public void init() {
        get("/", (req, res) -> "Hello World");
    }
}
