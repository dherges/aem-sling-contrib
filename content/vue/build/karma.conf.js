var webpackConf = require('./webpack.base.conf')
delete webpackConf.entry
webpackConf.devtool = '#inline-source-map'

module.exports = function (config) {
  config.set({
    browsers: ['PhantomJS'],
    frameworks: ['jasmine'],
    reporters: ['spec'],
    files: ['../src/main/content/jcr_root/apps/vue/test/unit/index.js'],
    preprocessors: {
      '../src/main/content/jcr_root/apps/vue/test/unit/index.js': ['webpack', 'sourcemap']
    },
    webpack: webpackConf,
    webpackMiddleware: {
      noInfo: true
    }
  })
}
