FROM node:10.10.0-alpine

COPY package.json /
COPY node_modules /
COPY build /
COPY script/docker/docker-entrypoint.sh /
RUN mkdir -p /app/build /applog /storage \
	&& mv /build/* /app/build/ \
  && mv /node_modules /app/ \
  && mv package.json /app/

VOLUME ["/app/config", "/applog", "/storage"]
EXPOSE 6000

CMD npm run start:$ENV_PHASE
