FROM node:10.10.0-alpine

COPY build /
COPY script/docker/docker-entrypoint.sh /
RUN mkdir -p /app /applog /storage \
	&& mv /build/* /app/ \

VOLUME ["/app/config", "/applog", "/storage"]
EXPOSE 6000

CMD npm run start:$ENV_PHASE
