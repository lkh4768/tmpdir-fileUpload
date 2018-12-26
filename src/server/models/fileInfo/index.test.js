import fileInfo from './index.js';

describe('fileInfo', () => {
  test('createEntity, Success', async () => {
    const beforeCreateTime = (new Date()).getTime();
    const newFileInfo = fileInfo.createEntity();
    const afterCreateTime = (new Date()).getTime();

    expect(typeof newFileInfo.id).toEqual('string');
    expect(newFileInfo.submissionTime).toBeGreaterThanOrEqual(beforeCreateTime);
    expect(newFileInfo.submissionTime).toBeLessThanOrEqual(afterCreateTime);
    expect(newFileInfo.expireTime).toEqual(__convertSubmissionTimeToExpireTime__(newFileInfo.submissionTime).getTime());
  });
});
