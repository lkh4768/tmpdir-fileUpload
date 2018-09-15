import express from 'express';
import Config from 'config';
import mongoose from 'mongoose';

import ConsoleLogger, { ExpressLoggerMiddleware } from '_modules/logger';
import apiRoutes from '_routes/api';

let appImpl;
let serverImpl;

const initLogger = () => {
  ConsoleLogger.info(process.env.NODE_ENV, 'NODE_ENV');
  ConsoleLogger.info(Config, 'config');
  ExpressLoggerMiddleware.use(appImpl);
};

const initMongo = async () => {
  let ret = false;
  try {
    await mongoose.connect(`${Config.get('db.url')}`, {
      user: Config.get('db.username'),
      pass: Config.get('db.password'),
      useNewUrlParser: true,
    });
    ret = true;
    ConsoleLogger.info('mongoose(%s) connect', Config.get('db.url'));
  } catch (err) {
    ConsoleLogger.error(
      err,
      'db(%s, { user: %s, pass: %s }) connect failed',
      Config.get('db.url'),
      Config.get('db.username'),
      Config.get('db.password'),
    );
  }
  return ret;
};

const initExpress = async () => {
  appImpl = express();
  appImpl.use('/api', apiRoutes);
  serverImpl = appImpl.listen(Config.get('server.port'));
  ConsoleLogger.info('localhost:%d listen', Config.get('server.port'));
};

const main = async () => {
  initExpress();
  initLogger();
  await initMongo();
};

main();

export const server = serverImpl;
export const app = appImpl;
