import bunyan from 'bunyan';

import LoggerUtils from './Utils';

class ConsoleLogger {
  constructor() {
    this.config = null;
    this.makeConfig();
    this.logger = bunyan.createLogger(this.config);
  }

  makeConfig() {
    this.config = {
      name: LoggerUtils.appName,
      level: LoggerUtils.level.console,
      src: true,
    };

    if (LoggerUtils.stream) {
      this.config.streams = [{ stream: LoggerUtils.stream }];
    }
  }

  fatal(...arg) {
    this.logger.fatal(...arg);
  }

  error(...arg) {
    this.logger.error(...arg);
  }

  warn(...arg) {
    this.logger.warn(...arg);
  }

  info(...arg) {
    this.logger.info(...arg);
  }

  debug(...arg) {
    this.logger.debug(...arg);
  }

  trace(...arg) {
    this.logger.trace(...arg);
  }
}

export default new ConsoleLogger();
