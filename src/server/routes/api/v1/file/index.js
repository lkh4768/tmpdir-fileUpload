import express from 'express';

const router = express.Router();

router.post('/', (req, res) => {
  res.json({ result: true });
});

export default router;
