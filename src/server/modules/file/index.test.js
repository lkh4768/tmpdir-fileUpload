import path from 'path';
import fs from 'fs';
import rimraf from 'rimraf';
import uuidv1 from 'uuid/v1';
import Config from 'config';
import mongoose from 'mongoose';

import file from './index.js';
import fileInfo from '_models/fileInfo';

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

let connection;

beforeAll(async () => {
  connection = await mongoose.connect(Config.get('db.url'), {
    useNewUrlParser: true,
  });
});

afterAll(async () => {
  await connection.disconnect();
});

describe('file', () => {
  test('store, Success', async () => {
    const root = Config.get('tmpdir.file.root');
    rimraf.sync(`${root}/*`);
    fs.copyFileSync(__testFilePath__, uploadedFiles[0].path);

    const fileId = uuidv1();
    await file.store(uploadedFiles, fileId);

    const newFilePath = path.join(root, fileId);
    const childFiles = fs.readdirSync(newFilePath);

    expect(childFiles.length).toEqual(1);
    expect(childFiles[0]).toEqual(uploadedFiles[0].originalname);
  });

  test('saveInRepo, Success', async () => {
    const newFileInfo = fileInfo.createEntity();
    const ret = await file.saveInRepo(newFileInfo);
    expect(ret.result).toEqual(true);

    expect(ret.data.id).toEqual(newFileInfo.id);
    expect(ret.data.submissionTime.getTime()).toEqual(newFileInfo.submissionTime);
    expect(ret.data.expireTime.getTime()).toEqual(newFileInfo.expireTime);
  });
});
