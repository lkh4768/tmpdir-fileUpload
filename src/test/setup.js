const path = require('path');
const fs = require('fs');
const MongodbMemoryServer = require('mongodb-memory-server');
const testConfig = require('../server/config/test.js');

const globalConfigPath = path.join(__dirname, 'globalConfig.json');

const mongod = new MongodbMemoryServer.default({
  instance: {
    dbName: 'test'
  },
  binary: {
    version: '3.2.18'
  }
});

module.exports = async function() {
  testConfig.db.url = await mongod.getConnectionString();

  global.__MONGOD__ = mongod;

  process.env.NODE_CONFIG = JSON.stringify(testConfig);
};
