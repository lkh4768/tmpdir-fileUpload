import bunyan from 'bunyan';

import LoggerUtils from './Utils';

class ConsoleLogger {
  constructor() {
    this.config = null;
    this.makeConfig();
    this.logger = bunyan.createLogger(this.config);
    this.fatal = this.logger.fatal.bind(this.logger);
    this.error = this.logger.error.bind(this.logger);
    this.warn = this.logger.warn.bind(this.logger);
    this.info = this.logger.info.bind(this.logger);
    this.debug = this.logger.debug.bind(this.logger);
    this.trace = this.logger.trace.bind(this.logger);
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
}

export default new ConsoleLogger();
