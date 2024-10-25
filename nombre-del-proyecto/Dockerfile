# Dockerfile for nombre-del-proyecto
FROM node:18-alpine

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

EXPOSE 3030

CMD ["npm", "start", "--", "-p", "3030"]

