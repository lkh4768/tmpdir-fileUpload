import request from 'supertest';
import Config from 'config';
import app, { server } from '../../../../index.js';

afterAll((done) => {
  server.close(done);
});

describe('file', () => {
  test('[router] POST /api/v1/file, Success', (done) => {
    const beforeSendTime = (new Date()).getTime();
    request(app)
      .post('/api/v1/file')
      .send('a')
      .expect(200)
      .end((err, res) => {
        if(err) {
          return done(err);
        }

        const afterSendTime = (new Date()).getTime();
        expect(typeof res.body.id === 'string').toEqual(true);
        expect(res.body.submissionTime).toBeGreaterThanOrEqual(beforeSendTime);
        expect(res.body.submissionTime).toBeLessThanOrEqual(afterSendTime);
        expect(res.body.expireTime).toEqual(res.body.submissionTime + Config.get('tmpdir.file.expireTermDay'));
        done();
    });
  });
});