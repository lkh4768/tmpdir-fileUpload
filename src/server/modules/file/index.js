import fs from 'fs';
import path from 'path';
import rimraf from 'rimraf';
import Config from 'config';

import ConsoleLogger from '_modules/logger';

const fsPromises = fs.promises;

const store = async (uploadedFiles, fileId) => {
  const root = path.join(Config.get('tmpdir.file.root'), fileId);
  try {
    await fsPromises.mkdir(root);
  } catch(err) {
    ConsoleLogger.error(err, 'mkdir(%s) failed', root);
  }

  for (const file of uploadedFiles) {
    try {
      await fsPromises.copyFile(file.path, path.join(root, file.originalname));
    } catch(err) {
      ConsoleLogger.error(
        err,
        'copyFile({ src: %s, dest: %s }) failed',
        file.path,
        path.join(root, file.originalname)
      );
    }

    rimraf(file.path, (err) => {
      ConsoleLogger.error(err, 'rm -rf %s failed', file.path);
    });
  }
};

export default {
  store,
};
