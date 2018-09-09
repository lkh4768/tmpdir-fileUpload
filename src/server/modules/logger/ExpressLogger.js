import expressBunyanLogger from 'express-bunyan-logger';

import LoggerUtils from './Utils';

const ExpressLogger = {
  create: (app) => {
    app.use(expressBunyanLogger(LoggerUtils.stream || null));
  },
};

export default ExpressLogger;
