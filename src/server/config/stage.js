const path = require('path');

module.exports = {
  name: 'tmpdir-fileupload-stage',
  server: {
    port: 6000,
  },
  tmpdir: {
    file: {
      expireTermDay: 1,
      maxSize: 1000 * 1000 * 1000,
      root: path.resolve(__dirname, '../../storage'),
    },
  },
  db: {
    url: 'mongodb://tmpdir:09WESdlatlwjwkdth@tmpdir-mongo-stage:27017/TMP_DIR',
  },
  log: {
    path: path.resolve(__dirname, '../../logs'),
    filename: 'tmpdir-fileupload-stage',
    level: {
      console: 'debug',
      express: 'info',
    },
    fileDateFormat: '%Y%m%d%H',
  },
};
