const path = require('path');

module.exports = {
  rootDir: path.join(__dirname, '../../'),
  verbose: true,
  globals: {
    __root__: path.join(__dirname, '../../'),
    __testFilename__: 'mb.txt',
    __testFilePath__: path.join(__dirname, '../../data/test/mb.txt'),
  },
  transform: {
    '^.+\\.js$': 'babel-jest'
  },
  modulePathIgnorePatterns: [
    'global.js',
    '<rootDir>/src/test/setup.js',
    '<rootDir>/src/test/teardown.js'
  ],
  moduleFileExtensions: ['js'],
  setupFiles: ['<rootDir>/src/test/global.js'],
  globalSetup: '<rootDir>/src/test/setup.js',
  globalTeardown: '<rootDir>/src/test/teardown.js',
  modulePathIgnorePatterns: ['setup.js', 'teardown.js', 'config/test.js'],
  moduleNameMapper: {
    '^_routes/(.*)$': '<rootDir>/src/server/routes/$1',
    '^_modules/(.*)$': '<rootDir>/src/server/modules/$1',
    '^_models/(.*)$': '<rootDir>/src/server/models/$1',
  },
  coverageDirectory: '<rootDir>/reports/coverage',
  reporters: ['default', 'jest-junit'],
};
