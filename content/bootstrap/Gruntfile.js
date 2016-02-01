/*
 * aem-sling-contrib
 * https://github.com/dherges/aem-sling-contrib
 *
 * Copyright (c) 2016 David Herges
 * Licensed under the MIT license.
 */

/*
 * Gruntfile.js: task defininitions for grunt
 * http://gruntjs.com/
 */
'use strict';

module.exports = function (grunt) {

  grunt.initConfig({
    pkg:     grunt.file.readJSON('package.json'),       // node dependencies
    bwr:     grunt.file.readJSON('bower.json'),         // bower dependencies
    bwrdir:  grunt.file.readJSON('.bowerrc').directory, // bower directory
    jcrroot: "content/jcr_root",
    myproject: {
      clientlibs: { // paths in '/etc/cliebtlibs'
        public: {
          styles: "<%= jcrroot %>/etc/clientlibs/bootstrap/public/styles",
          scripts: "<%= jcrroot %>/etc/clientlibs/bootstrap/public/scripts"
        },
        vendor: "<%= jcrroot %>/etc/clientlibs/bootstrap/vendor"
      }
    },

    // grunt less
    less: {
      options: {
        relativeUrls: false,
        strictMath: true,
        compress: true,
        // Configure source map pathes so that URLs are serverd by AEM server; see: http://stackoverflow.com/a/21769730
        sourceMap: false,
        sourceMapRootpath: "/",
        sourceMapBasepath: "<%= jcrroot %>"
      },
      // grunt less:public
      public: {
        files: [{
          expand: true,
          cwd: '<%= myproject.clientlibs.public.styles %>',
          src: ['mybootstrap.less'],
          dest: '<%= myproject.clientlibs.public.styles %>',
          ext: '.css'
        }]
      }
    },


    // grunt bower
    bower: {
      install: {},
      list: {}
    },

    // grunt copy
    copy: {
      // grunt copy:clientlibs
      clientlibs: {
        files: [
          // copies client libraries from 'bower_components' to the target directory, renaming folders containing a '.'
          {
            expand: true,
            cwd: '<%= bwrdir %>',
            src: ['*/**'],
            dot: true,
            rename: function (dest, src) {

              // replace '.' in directory names with a blank
              var srcPathComponents = src.split('/');
              for (var i = 0, len = (srcPathComponents && srcPathComponents.length); i < ((len - 1) || 1); i += 1) {
                srcPathComponents[i] = srcPathComponents[i].replace('.', '');
              }
              return grunt.config.get('myproject.clientlibs.vendor') + '/' + srcPathComponents.join('/');
            }
          }
        ]
      }
    }
  });

  grunt.event.on('bower.end', function (data) {
    // get bower components from result and schedule the 'generateClientLibraries' task
    if (data && data.endpoint && data.dependencies) {
      grunt.config.set('bower_components', data.dependencies);
      grunt.task.run('generateClientLibraries');
    }
  });

  /**
   * AEM-enabling grunt task that generates a '.content.xml' files in the target folder of each bower dependency so
   * that bower packages are recognized as client libraries in AEM
   */
  grunt.registerTask(
    'generateClientLibraries',
    'Generates a \'.content-xml\' for each bower dependency so that it becomes a \'cq:ClientLibraryFolder\' that is recognized by AEM.',
    function() {
      var aemFriendlyPkgName = function(pkgName) { // 'typeahead.js' is a very evil clientlib name for AEM
        return "myproject.vendor." + pkgName.replace('.', '');
      };
      grunt.config.requires('bower_components');
      var components = grunt.config.get('bower_components');

      for (var compName in components) {
        var comp = components[compName];

        // file path: '<%= bwrdir %>/{package}/.content.xml'
        var path = grunt.template.process('<%= dir %>/<%= pkgDirectory %>/.content.xml', {
          data: {
            dir: grunt.config.get('bwrdir'),
            pkgDirectory: comp.endpoint.name
          }
        });

        var dependencies = grunt.util
          .toArray(comp.dependencies)
          .map(function(dependency) {
            return aemFriendlyPkgName(dependency.pkgMeta.name)
          });

        // XXX: quick hack, 'procato.jquery' will depend on 'cq.jquery'
        if (compName === 'jquery') {
          dependencies.push('cq.jquery')
        }

        // .content.xml template
        var xmlTemplate = '<?xml version="1.0" encoding="UTF-8"?>' + "\n" +
          '<jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"' + "\n" +
          '    jcr:primaryType="cq:ClientLibraryFolder"' + "\n" +
          '    categories="[<%= pkgName %>]"' + "\n" +
          '    dependencies="[<%= deps.join(",") %>]"/>';

        var xmlContents = grunt.template.process(xmlTemplate, {
          data: {
            pkgName: aemFriendlyPkgName(compName),
            deps: dependencies
          }
        });

        grunt.file.write(path, xmlContents);
        grunt.log.ok("Wrote client library folder: " + path);
      }
  });


  // Load task definitions and grunt plugins
  grunt.loadNpmTasks('grunt-bower-event');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-modernizr');

  // Alias Tasks
  grunt.registerTask('install', ['bower:install', 'bower:list', 'copy:clientlibs']);

}
