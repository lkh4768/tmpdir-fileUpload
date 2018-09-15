import express from 'express';
import uuidv1 from 'uuid/v1';
import multer from 'multer';
import Config from 'config';
import path from 'path';

import ConsoleLogger from '_modules/logger';
import file from '_modules/file';

const router = express.Router();
const upload = multer({
  dest: Config.get('tmpdir.file.root'),
  limits: { fileSize: Config.get('tmpdir.file.maxSize') },
});

router.post('/', upload.array('file'), async (req, res) => {
  ConsoleLogger.info(req.files, 'Recv files');
  const id = uuidv1();
  const currentDate = (new Date()).getTime();
  file.store(req.files, id);
  res.json({ id, submissionTime: currentDate, expireTime: currentDate+1 });
});

export default router;
