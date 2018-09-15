import express from 'express';
import multer from 'multer';
import Config from 'config';

import ConsoleLogger from '_modules/logger';
import file from '_modules/file';
import fileInfo from '_models/fileInfo';

const router = express.Router();
const upload = multer({
  dest: Config.get('tmpdir.file.root'),
  limits: { fileSize: Config.get('tmpdir.file.maxSize') },
});

router.post('/', upload.array('file'), async (req, res) => {
  ConsoleLogger.info('Recv files', req.files);
  const fileInfoEntity = fileInfo.createEntity();
  file.store(req.files, fileInfoEntity.id);
  const ret = await file.saveInRepo(fileInfoEntity);
  if (!ret.result) {
    return res.sendStatus(500).end();
  }
  return res.json(fileInfoEntity).end();
});

export default router;
