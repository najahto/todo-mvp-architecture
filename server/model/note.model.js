const mongoose = require('mongoose');

const noteSchema = mongoose.Schema({
  title: {
    type: String,
    required: true,
  },
  note: {
    type: String,
    default: '',
    required: true,
  },
  color: {
    type: Number,
    default: 000000,
  },
  date: {
    type: Date,
    default: Date.now,
  },
  success: {
    type: Boolean,
    default: false,
  },
  message: {
    type: String,
    default: '',
  },
});

noteSchema.virtual('id').get(function () {
  return this._id.toHexString();
});

noteSchema.set('toJSON', {
  virtuals: true,
});

exports.Note = mongoose.model('Note', noteSchema);
