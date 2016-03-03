/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

package de.spektrakel.sling.resources.rest;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteTest {

    @Test
    public void testNotNull() {
        assertThat(new Route("/abc"))
                .isNotNull();
    }

    @Test
    public void pathMustStartWithSlash() {
        try {
            new Route("abc");
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("!");
        }
    }

    @Test
    public void pathMustNotEndWithSlash() {
        try {
            new Route("/abc/");
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("!");
        }
    }

    @Test
    public void pathMustBeReturned() {
        assertThat(new Route("/my/path").getPath())
                .isEqualTo("/my/path");
    }

    @Test
    public void pathPartsMustBeReturned() {
        assertThat(new Route("/my/path").getPathSegments())
                .hasSize(2)
                .containsExactly("my", "path");
    }

    @Test
    public void pathParamsMustBeReturned() {
        assertThat(new Route("/my/path").getPathParams())
                .isEmpty();

        assertThat(new Route("/my/:path").getPathParams())
                .hasSize(1)
                .containsExactly(":path");
    }

    @Test
    public void pathParamsMustBeChecked() {
        assertThat(new Route("/my/path").hasPathParam(":path"))
                .isFalse();

        assertThat(new Route("/my/path").hasPathParam("path"))
                .isFalse();

        assertThat(new Route("/my/:path").hasPathParam(":path"))
                .isTrue();

        assertThat(new Route("/my/:path").hasPathParam("path"))
                .isFalse();
    }

    @Test
    public void matchesForPath() {
        assertThat(new Route("/my/path").matches("/my/path").decision())
                .isTrue();

        assertThat(new Route("/my/path").matches("/my/other/thing").decision())
                .isFalse();
    }

    @Test
    public void matchesForParams() {
        assertThat(new Route("/my/:path").matches("/my/path").decision())
                .isTrue();

        assertThat(new Route("/my/:path").matches("/my/123").decision())
                .isTrue();

        assertThat(new Route("/my/:path").matches("/my/other/thing").decision())
                .isFalse();
    }

    @Test
    public void matchesReturnInput() {
        assertThat(new Route("/my/:path").matches("/my/path").input())
                .isEqualTo("/my/path");
    }

    @Test
    public void matchesReturnRoute() {
        final Route route = new Route("/my/:path");
        assertThat(route.matches("/my/path").route())
                .isSameAs(route);
    }

    @Test
    public void matchesReturnParam() {
        assertThat(new Route("/my/:path").matches("/my/path").param(":path"))
                .isEqualTo("path");

        final Route.Match match = new Route("/my/:path/:id").matches("/my/book/780");
        assertThat(match.param(":path"))
                .isEqualTo("book");

        assertThat(match.param(":id"))
                .isEqualTo("780");
    }

    @Test
    public void matchesReturnParamNames() {
        assertThat(new Route("/my/:path").matches("/my/path").paramNames())
                .containsOnly(":path");

        assertThat(new Route("/my/:path/:id").matches("/my/book/780").paramNames())
                .containsOnly(":path", ":id");
    }

}
