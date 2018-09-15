import request from 'supertest';
import Config from 'config';
import rimraf from 'rimraf';
import fs from 'fs';
import path from 'path';
import { app, server } from '../../../../index.js';

afterAll((done) => {
  server.close(done);
});

describe('file', () => {
  test('[router] POST /api/v1/file, Success', (done) => {
    const fileRootPath = Config.get('tmpdir.file.root');
    rimraf.sync(`${fileRootPath}/*`);
    const beforeSendTime = (new Date()).getTime();
    request(app)
      .post('/api/v1/file')
      .attach('file', '/home/wes/storage/workspace/tmpdir-fileUpload/data/test/mb.txt')
      .expect(200)
      .end((err, res) => {
        if(err) {
          return done(err);
        }

        // check fileInfo
        const afterSendTime = (new Date()).getTime();
        expect(typeof res.body.id === 'string').toEqual(true);
        expect(res.body.submissionTime).toBeGreaterThanOrEqual(beforeSendTime);
        expect(res.body.submissionTime).toBeLessThanOrEqual(afterSendTime);
        expect(res.body.expireTime).toEqual(__convertSubmissionTimeToExpireTime__(res.body.submissionTime).getTime());
        done();
    });
  });
});
