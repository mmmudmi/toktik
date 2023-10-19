FROM node:16 as builder

WORKDIR /app

COPY /package.json /yarn.lock ./
COPY ./ ./
RUN npm install axios
RUN npm install jwt-check-expiration

# RUN yarn install
# RUN yarn build
RUN npm install
RUN npm run build

FROM nginx:1.21-alpine
COPY --from=builder /app/dist /usr/share/nginx/html/
