import mongoose from 'mongoose';
import uuidv1 from 'uuid/v1';

const fileInfoSchema = new mongoose.Schema({
  id: String,
  submissionTime: Date,
  expireTime: Date,
});

const FileInfo = mongoose.model('FileInfo', fileInfoSchema);

const createEntity = () => {
  const submissionTime = new Date();
  const expireTime = new Date(submissionTime);
  expireTime.setDate(expireTime.getDate() + 1);

  return {
    id: uuidv1(),
    submissionTime: submissionTime.getTime(),
    expireTime: expireTime.getTime(),
  };
};

export default {
  FileInfo,
  createEntity,
};
