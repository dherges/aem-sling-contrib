/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

define([
	"../var/support"
], function( support ) {

support.focusinBubbles = "onfocusin" in window;

return support;

});
