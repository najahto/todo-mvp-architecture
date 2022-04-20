const { Note } = require('../model/note.model');

/**
 * Create Note
 */
const createNote = async (req, res) => {
  let note = new Note({
    title: req.body.title,
    note: req.body.note,
    color: req.body.color,
    date: req.body.date,
    success: req.body.success,
    message: req.body.message,
  });
  note = await note.save();
  if (!note) return res.status(400).send('the Note cannot be created!');

  res.status(201).json({
    success: true,
    message: 'the Note successfully created',
    Note: note,
  });
};

/**
 * Find all notes
 */
const getNotes = async (req, res) => {
  const notes = await Note.find();
  if (!notes) {
    res.status(500).json({
      success: false,
    });
  }
  res.status(200).send(notes);
};

/**
 *  Find Note by Id
 */
const findNote = async (req, res) => {
  const note = await Note.findById(req.params.id);
  if (!note) {
    res.status(404).json({
      success: false,
      message: 'the Note with the given Id was not found.',
    });
  }
  res.status(200).send(note);
};

/**
 * Update Note
 */
const updateNote = async (req, res) => {
  const note = await Note.findByIdAndUpdate(
    req.params.id,
    {
      title: req.body.title,
      note: req.body.note,
      color: req.body.color,
      date: req.body.date,
      success: req.body.success,
      message: req.body.message,
    },
    {
      new: true,
    }
  );

  if (!note) return res.status(400).send('the Note cannot be updated!');

  res.status(200).json({
    success: true,
    message: 'the Note successfully updated',
    Note: note,
  });
};

/**
 * remove Note
 */
const deleteNote = async (req, res) => {
  Note.findByIdAndRemove(req.params.id)
    .then((note) => {
      if (note) {
        return res.status(200).json({
          success: true,
          message: 'the Note deleted successfully',
        });
      } else {
        res.status(404).json({ success: false, message: 'Note not found!' });
      }
    })
    .catch((err) => {
      return res.status(400).json({ success: false, error: err });
    });
};

module.exports = {
  createNote,
  getNotes,
  findNote,
  updateNote,
  deleteNote,
};
