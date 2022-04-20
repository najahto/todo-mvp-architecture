const express = require('express');
const router = express.Router();
const noteController = require('../controller/note.controller');

router.post('/', noteController.createNote);

router.get('/', noteController.getNotes);

router.get('/:id', noteController.findNote);

router.put('/:id', noteController.updateNote);

router.delete('/:id', noteController.deleteNote);

module.exports = router;
