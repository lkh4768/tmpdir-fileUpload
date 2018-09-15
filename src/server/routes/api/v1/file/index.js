import express from 'express';
import multer from 'multer';
import Config from 'config';
import path from 'path';

import ConsoleLogger from '_modules/logger';
import file from '_modules/file';
import fileInfo from '_models/fileInfo';

const router = express.Router();
const upload = multer({
  dest: Config.get('tmpdir.file.root'),
  limits: { fileSize: Config.get('tmpdir.file.maxSize') },
});

router.post('/', upload.array('file'), async (req, res) => {
  ConsoleLogger.info(req.files, 'Recv files');
  const newFileInfo = fileInfo.create();
  file.store(req.files, newFileInfo.id);
  res.json(newFileInfo);
});

export default router;
