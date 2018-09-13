import express from 'express';
import uuidv1 from 'uuid/v1';
import multer from 'multer';
import Config from 'config';
import fs from 'fs';
import path from 'path';
import rimraf from 'rimraf';

import ConsoleLogger from '_modules/logger';

const fsPromises = fs.promises;

const router = express.Router();
const upload = multer({
  dest: Config.get('tmpdir.file.root'),
  limits: { fileSize: Config.get('tmpdir.file.maxSize') },
});

router.post('/', upload.array('file'), async (req, res) => {
  console.log(req.files);
  const id = uuidv1();
  const newRoot = path.join(Config.get('tmpdir.file.root'), id);
  fsPromises.mkdir(newRoot).then(() => {
    req.files.forEach((file) => {
      fsPromises.copyFile(file.path, path.join(newRoot, file.originalname))
        .then(() => {
          rimraf(file.path, (err) => {
            ConsoleLogger.error(err, 'rm -rf %s failed', file.path);
          });
        })
        .catch((err) => {
          ConsoleLogger.error(err, 'copyFile({ src: %s, dest: %s }) failed', file.path, path.join(newRoot, file.originalname));
        });
    });
  }).catch((err) => {
    ConsoleLogger.error(err, 'mkdir(%s) failed', newRoot);
  });
  const currentDate = (new Date()).getTime();
  res.json({ id, submissionTime: currentDate, expireTime: currentDate+1 });
});

export default router;
