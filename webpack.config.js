const path = require('path');
const nodeExternals = require('webpack-node-externals');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const config = {
  mode: 'none',
  name: 'server',
  entry: {
    index: path.resolve(__dirname, 'src/server/index.js'),
  },
  output: {
    path: path.resolve(__dirname, 'build/'),
    filename: '[name].js',
    publicPath: '/',
  },
  target: 'node',
  node: {
    __filename: true,
    __dirname: true,
  },
  externals: [nodeExternals()],
  resolve: {
    extensions: ['.js'],
    modules: [
      path.resolve(__dirname, 'node_modules'),
    ],
    alias: {
      _modules: path.resolve(__dirname, 'src/server/modules/'),
      _routes: path.resolve(__dirname, 'src/server/routes/'),
      _models: path.resolve(__dirname, 'src/server/models/'),
    },
  },
  module: {
    rules: [
      {
        enforce: 'pre',
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'eslint-loader',
        options: {
          emitWarning: true,
          configFile: './.eslintrc.js',
        },
      },
    ],
  },
  plugins: [
    new CopyWebpackPlugin([{
      from: path.resolve(__dirname, `src/server/config`),
      to: path.resolve(__dirname, `build/config`),
      toType: 'dir'
    }]),
  ],
};

module.exports = config;
