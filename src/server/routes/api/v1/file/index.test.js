import request from 'supertest';
import Config from 'config';
import rimraf from 'rimraf';

import file from '_modules/file';
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

        const afterSendTime = (new Date()).getTime();
        expect(typeof res.body.id === 'string').toEqual(true);
        expect(res.body.submissionTime).toBeGreaterThanOrEqual(beforeSendTime);
        expect(res.body.submissionTime).toBeLessThanOrEqual(afterSendTime);
        expect(res.body.expireTime).toEqual(__convertSubmissionTimeToExpireTime__(res.body.submissionTime).getTime());
        return done();
    });
  });

  test('[router] POST /api/v1/file, Empty file', (done) => {
    const fileRootPath = Config.get('tmpdir.file.root');
    rimraf.sync(`${fileRootPath}/*`);
    request(app)
      .post('/api/v1/file')
      .expect(400)
      .end((err, res) => {
        if(err) {
          return done(err);
        }
        expect(res.text).toEqual('Not found files');
        return done();
    });
  });

  test('[router] POST /api/v1/file, save fileInfo in repo failed', (done) => {
    const fileRootPath = Config.get('tmpdir.file.root');
    rimraf.sync(`${fileRootPath}/*`);

    file.saveInRepo = jest.fn();
    file.saveInRepo.mockResolvedValue({ result: false });

    request(app)
      .post('/api/v1/file')
      .attach('file', '/home/wes/storage/workspace/tmpdir-fileUpload/data/test/mb.txt')
      .expect(500)
      .end((err, res) => {
        if(err) {
          return done(err);
        }
        return done();
    });
  });
});
