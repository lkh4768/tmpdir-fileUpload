import fs from 'fs';
import path from 'path';
import rmfr from 'rmfr';
import Config from 'config';

import ConsoleLogger from '_modules/logger';
import fileInfo from '_models/fileInfo';

const fsPromises = fs.promises;

const store = async (uploadedFiles, fileId) => {
  try {
    await fsPromises.access(Config.get('tmpdir.file.root'));
  } catch (err) {
    try {
      await fsPromises.mkdir(Config.get('tmpdir.file.root'));
      ConsoleLogger.info('mkdir(%s) success', Config.get('tmpdir.file.root'));
    } catch (mkdirErr) {
      ConsoleLogger.error('mkdir(%s) failed', Config.get('tmpdir.file.root'), mkdirErr);
      return { result: false };
    }
  }

  const root = path.join(Config.get('tmpdir.file.root'), fileId);
  try {
    await fsPromises.mkdir(root);
    ConsoleLogger.info('mkdir(%s) success', root);
  } catch (err) {
    ConsoleLogger.error('mkdir(%s) failed', root, err);
    return { result: false };
  }

  const rets = await Promise.all(uploadedFiles.map(async (file) => {
    const destPath = path.join(root, file.originalname);
    const srcPath = file.path;
    try {
      await fsPromises.copyFile(srcPath, destPath);
    } catch (err) {
      ConsoleLogger.error(
        'copyFile({ src: %s, dest: %s }) failed',
        srcPath,
        destPath,
        err,
      );
      return { result: false, path: destPath };
    }
    ConsoleLogger.info(
      'copyFile({ src: %s, dest: %s }) success',
      srcPath,
      destPath,
    );

    try {
      await rmfr(srcPath);
    } catch (err) {
      ConsoleLogger.error('rm -rf %s failed', srcPath, err);
      return { result: false, path: destPath };
    }
    ConsoleLogger.info('rm -rf %s success', srcPath);
    return { result: true, path: destPath };
  }));

  return { result: rets.every(ret => ret.result === true), data: rets };
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
