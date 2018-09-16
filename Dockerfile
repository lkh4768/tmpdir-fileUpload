FROM node:10.10.0-alpine

COPY package.json /package.json
COPY node_modules /node_modules
COPY build /build
RUN mkdir -p /app /applog /storage \
	&& mv /build /app/ \
  && mv /node_modules /app/ \
  && mv /package.json /app/

VOLUME ["/app/config", "/applog", "/storage"]
EXPOSE 6000

CMD npm run start:$ENV_PHASE
