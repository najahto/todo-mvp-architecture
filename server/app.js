const express = require('express');
const app = express();
const mongoose = require('mongoose');
const cors = require('cors');
app.use(cors());
require('dotenv/config');

app.options('*', cors());
app.use(express.json());

const base_url = process.env.APP_URL;

// Routers
const noteRouter = require('./routes/notes');
app.use(`${base_url}/notes`, noteRouter);

// Database connection
mongoose
  .connect(process.env.CONNECTION_STRING, {
    dbName: 'todo-db',
  })
  .then(() => {
    console.log('Database connection is ready ...');
  })
  .catch((err) => {
    console.log(err);
  });

// server
app.listen(3000, () => {
  console.log('server is running ');
});
