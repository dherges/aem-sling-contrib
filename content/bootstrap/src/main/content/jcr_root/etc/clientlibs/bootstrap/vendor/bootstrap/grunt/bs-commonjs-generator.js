/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

'use strict';

var fs = require('fs');
var path = require('path');

var COMMONJS_BANNER = '// This file is autogenerated via the `commonjs` Grunt task. You can require() this file in a CommonJS environment.\n';

module.exports = function generateCommonJSModule(grunt, srcFiles, destFilepath) {
  var destDir = path.dirname(destFilepath);

  function srcPathToDestRequire(srcFilepath) {
    var requirePath = path.relative(destDir, srcFilepath).replace(/\\/g, '/');
    return 'require(\'' + requirePath + '\')';
  }

  var moduleOutputJs = COMMONJS_BANNER + srcFiles.map(srcPathToDestRequire).join('\n');
  try {
    fs.writeFileSync(destFilepath, moduleOutputJs);
  } catch (err) {
    grunt.fail.warn(err);
  }
  grunt.log.writeln('File ' + destFilepath.cyan + ' created.');
};
