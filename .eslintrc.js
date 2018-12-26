module.exports = {
  'parser': 'babel-eslint',
  'env': {
    'browser': true
  },
  'extends': [
    'airbnb'
  ],
  'plugins': [
    'import',
  ],
  'settings': {
    'import/resolver': {
      'webpack': {
        'config': 'webpack.config.js'
      },
    }
  },
};
