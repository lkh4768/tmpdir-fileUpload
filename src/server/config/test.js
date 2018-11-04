const path = require('path');

module.exports = {
  name: 'tmpdir-fileupload-test',
  server: {
    port: 6000,
  },
  tmpdir: {
    file: {
      expireTermDay: 1,
      maxSize: 1000 * 1000 * 1000,
      root: path.resolve(__dirname, '../../../storage'),
    },
  },
  db: {
    url: 'mongodb://dev.sw2h.xyz:7017/TMP_DIR',
  },
  log: {
    path: path.resolve(__dirname, '../../../logs'),
    filename: 'tmpdir-fileupload-test',
    level: {
      console: 'debug',
      express: 'info',
    },
    fileDateFormat: '%Y%m%d%H',
  },
};
