import express from 'express';
import uuidv1 from 'uuid/v1';
import multer from 'multer';
import Config from 'config';

const router = express.Router();
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    console.log(Config.get('tmpdir.file.root'));
    cb(null, Config.get('tmpdir.file.root'));
  }
});
const upload = multer({ storage });

router.post('/', upload.array(), (req, res) => {
  const currentDate = (new Date()).getTime();
  res.json({ id: '1', submissionTime: currentDate, expireTime: currentDate+1 });
});

export default router;
