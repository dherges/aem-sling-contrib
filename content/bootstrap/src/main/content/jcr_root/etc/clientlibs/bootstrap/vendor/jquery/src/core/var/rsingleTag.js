/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

define(function() {
	// Match a standalone tag
	return (/^<(\w+)\s*\/?>(?:<\/\1>|)$/);
});
