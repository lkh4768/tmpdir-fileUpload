import path from 'path';
import fs from 'fs';
import config from 'config';
import RotatingFileStream from 'bunyan-rotating-file-stream';

import Const from '_modules/common/const';

const logConfig = config.get('log');

const LoggerUtils = {
  stream: null,
  path: logConfig.path,
  filename: logConfig.filename,
  fileDateFormat: logConfig.fileDateFormat,
  appName: config.get('name') || Const.defaultLoggerConfig.name,
  level: {
    console: logConfig.level.console || Const.defaultLoggerConfig.level,
    express: logConfig.level.express || Const.defaultLoggerConfig.level,
  },
  hasConfigForStream: f => f,
};

LoggerUtils.hasConfigForStream = () => LoggerUtils.path
  && LoggerUtils.filename
  && LoggerUtils.fileDateFormat;

const makeStream = () => {
  if (!fs.existsSync(LoggerUtils.path)) {
    fs.mkdirSync(LoggerUtils.path);
  }

  return new RotatingFileStream({
    path: [path.join(LoggerUtils.path, LoggerUtils.filename), '_', LoggerUtils.fileDateFormat, '.log'].join(''),
    period: '1h',
    gzip: true,
  });
};

if (LoggerUtils.hasConfigForStream()) {
  LoggerUtils.stream = makeStream();
}

export default LoggerUtils;
