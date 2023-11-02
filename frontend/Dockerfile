FROM node:16 as builder

WORKDIR /app

COPY /package.json /yarn.lock ./
COPY ./ ./
RUN yarn add axios
RUN yarn add jwt-check-expiration
RUN yarn add i video.js
RUN yarn install
RUN yarn build

FROM nginx:1.21-alpine
COPY --from=builder /app/dist /usr/share/nginx/html/
