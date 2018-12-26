import path from 'path';
import fs from 'fs';
import rmfr from 'rmfr';
import uuidv1 from 'uuid/v1';
import Config from 'config';
import mongoose from 'mongoose';

import file from './index.js';
import fileInfo from '_models/fileInfo';

const fsPromises = fs.promises;
const fileId = uuidv1();
const newFileDir = path.join(Config.get('tmpdir.file.root'), fileId);
const uploadedFiles = [
	{
		fieldname: 'file',
    originalname: __testFilename__,
    encoding: '7bit',
    mimetype: 'text/plain',
    destination: Config.get('tmpdir.file.root'),
    filename: 'md.txt',
    path: path.join(Config.get('tmpdir.file.root'), __testFilename__),
    size: 1000000,
  },
];
const newFilePath = path.join(newFileDir, uploadedFiles[0].originalname);

let connection;

beforeAll(async () => {
  connection = await mongoose.connect(Config.get('db.url'), {
    useNewUrlParser: true,
  });
});

afterAll(async () => {
  await connection.disconnect();
});

beforeEach(async () => {
  await jest.resetAllMocks();
  try {
    await fsPromises.access(Config.get('tmpdir.file.root'));
  } catch (err) {
    await fsPromises.mkdir(Config.get('tmpdir.file.root'));
  }
  fs.copyFileSync(__testFilePath__, uploadedFiles[0].path);
  await rmfr(newFileDir);
});

describe('file', () => {
  test('store, Success', async () => {
    const rets = await file.store(uploadedFiles, fileId);

    expect(rets).toEqual({
      result: true,
      data: [{ result: true, path: newFilePath }],
    });
    rets.data.forEach(ret => {
      expect(ret.result).toEqual(true);
      expect(fsPromises.access(ret.path)).resolves.toBeUndefined();
    });
  });

  test('store, mkdir root/fileId failed', async () => {
    fsPromises.mkdir = jest.fn();
    fsPromises.mkdir.mockRejectedValue(false);

    const rets = await file.store(uploadedFiles, '');

    expect(rets.result).toEqual(false);
  });

  test('store, CopyFile srcPath/destPath failed', async () => {
    fsPromises.copyFile = jest.fn();
    fsPromises.copyFile.mockRejectedValue(false);

    const rets = await file.store(uploadedFiles, fileId);

    expect(rets).toEqual({
      result: false,
      data: [{ result: false, path: newFilePath }],
    });
    rets.data.forEach(ret => {
      expect(ret.result).toEqual(false);
      expect(fsPromises.access(ret.path)).rejects.toThrow();
    });
  });

  test('saveInRepo, Success', async () => {
    const newFileInfo = fileInfo.createEntity();
    const ret = await file.saveInRepo(newFileInfo);
    expect(ret.result).toEqual(true);

    expect(ret.data.id).toEqual(newFileInfo.id);
    expect(ret.data.submissionTime.getTime()).toEqual(newFileInfo.submissionTime);
    expect(ret.data.expireTime.getTime()).toEqual(newFileInfo.expireTime);
  });

  test('saveInRepo, Save fileInfo failed', async () => {
    fileInfo.FileInfo = jest.fn(function() {
      this.save = new Promise((resolve, reject) => { reject(false); });
    });

    const newFileInfo = fileInfo.createEntity();
    const ret = await file.saveInRepo(newFileInfo);

    expect(ret.result).toEqual(false);
    expect(ret.data).toEqual(null);
  });
});
