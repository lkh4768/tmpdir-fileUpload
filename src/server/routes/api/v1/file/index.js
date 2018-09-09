import express from 'express';

const router = express.Router();

router.post('/', (req, res) => {
  const currentDate = (new Date()).getTime();
  res.json({ id: '1', submissionTime: currentDate, expireTime: currentDate+1 });
});

export default router;
