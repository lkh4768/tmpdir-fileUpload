import Config from 'config';
// method
global.__convertSubmissionTimeToExpireTime__ = (submissionTime) => {
  const expireTime = new Date(submissionTime);
  expireTime.setDate(expireTime.getDate() + Config.get('tmpdir.file.expireTermDay'));
  return expireTime;
};
