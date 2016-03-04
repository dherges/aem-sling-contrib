/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.routing.sinatra;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RouterTest {


    @Test
    public void buildsNotNull() {
        assertThat(new Router.Builder()
                .build()
        ).isNotNull();

        assertThat(new Router.Builder()
                .addRoute(new Route("/abc"))
                .build()
        ).isNotNull();

        assertThat(new Router.Builder()
                .addRoute(new Route("/abc"))
                .basePath("/my/base/path")
                .build()
        ).isNotNull();
    }

    @Test
    public void findMatch() {
        final Router router = new Router.Builder()
                .basePath("/some/basement")
                .addRoute(new Route("/:id"))
                .build();

        final Route.Match match = router.findMatch("/some/basement/345");
        assertThat(match).isNotNull();
        assertThat(match.isMatch()).isTrue();
        assertThat(match.param(":id")).isEqualTo("345");

        final Route.Match noMatch = router.findMatch("/some/shitty/things/over/here");
        assertThat(noMatch).isNotNull();
        assertThat(noMatch.isMatch()).isFalse();
    }


}
