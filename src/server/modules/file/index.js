import fs from 'fs';
import path from 'path';
import rimraf from 'rimraf';
import Config from 'config';

import ConsoleLogger from '_modules/logger';
import fileInfo from '_models/fileInfo';

const fsPromises = fs.promises;

const store = async (uploadedFiles, fileId) => {
  const root = path.join(Config.get('tmpdir.file.root'), fileId);
  try {
    await fsPromises.mkdir(root);
    ConsoleLogger.info('mkdir(%s) success', root);
  } catch (err) {
    ConsoleLogger.error('mkdir(%s) failed', root, err);
    return false;
  }

  Promise.all(uploadedFiles.map(async (file) => {
    try {
      await fsPromises.copyFile(file.path, path.join(root, file.originalname));
      ConsoleLogger.info(
        'copyFile({ src: %s, dest: %s }) success',
        file.path,
        path.join(root, file.originalname),
      );
    } catch (err) {
      ConsoleLogger.error(
        'copyFile({ src: %s, dest: %s }) failed',
        file.path,
        path.join(root, file.originalname),
        err,
      );
    }

    rimraf(file.path, (err) => {
      if (err) {
        ConsoleLogger.error('rm -rf %s failed', file.path, err);
      }
      ConsoleLogger.info('rm -rf %s success', file.path);
    });
  }));

  return true;
};

const saveInRepo = async (fileInfoEntity) => {
  const newFileInfo = new fileInfo.FileInfo(fileInfoEntity);
  let ret;
  try {
    ret = {
      data: await newFileInfo.save(),
      result: true,
    };
    ConsoleLogger.info(
      'Save fileInfoEntity(%O) in repo success',
      fileInfoEntity,
    );
  } catch (err) {
    ConsoleLogger.error(
      'Save fileInfoEntity(%O) in repo failed\n',
      fileInfoEntity,
      err,
    );
    ret = { result: false, data: null };
  }
  return ret;
};

export default {
  store,
  saveInRepo,
};
