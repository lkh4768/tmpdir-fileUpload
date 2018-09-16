FROM node:10.10.0-alpine

COPY package.json /package.json
COPY node_modules /node_modules
COPY build/config /config
COPY build/index.js /index.js
COPY script/docker/docker-entrypoint.sh /
RUN mkdir -p /app/build/config /applog /storage \
	&& mv /index.js /app/build \
  && mv /node_modules /app/ \
  && mv /package.json /app/ \
  && chmod +x /docker-entrypoint.sh

VOLUME ["/app/build/config", "/applog", "/storage"]
EXPOSE 6000

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD npm run start:$ENV_PHASE
