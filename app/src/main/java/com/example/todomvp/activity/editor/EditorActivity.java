package com.example.todomvp.activity.editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todomvp.R;
import com.example.todomvp.api.ApiClient;
import com.example.todomvp.api.ApiInterface;
import com.example.todomvp.model.Note;
import com.thebluealliance.spectrum.SpectrumPalette;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity implements EditorView {

    EditText etTitle, etNote;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    EditorPresenter presenter;

    int color;
    String id, title, note;

    Menu actionMenu;

    private final static String UPDATE_NOTE = "Update note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNote);
        palette = findViewById(R.id.palette);

        palette.setOnColorSelectedListener(
                clr -> color = clr
        );
        // Default color
        palette.setSelectedColor(getResources().getColor(R.color.white));
        color = getResources().getColor(R.color.white);

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        presenter = new EditorPresenter(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("EXTRA_ID");
        title = intent.getStringExtra("EXTRA_TITLE");
        note = intent.getStringExtra("EXTRA_NOTE");
        color = intent.getIntExtra("EXTRA_COLOR", 0);

        setDataFromIntentExtra();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if (id != null) {
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = etTitle.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        int color = this.color;

        switch (item.getItemId()) {
            case R.id.save:
                // save
                if (title.isEmpty()) {
                    etTitle.setError("Please enter a title ");
                } else if (note.isEmpty()) {
                    etNote.setError("Please enter a note");
                } else {
                    presenter.saveNote(title, note, color);
                }
                return true;

            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);
                actionMenu.findItem(R.id.save).setVisible(false);
                return true;

            case R.id.update:
                //update
                if (title.isEmpty()) {
                    etTitle.setError("Please enter a title ");
                } else if (note.isEmpty()) {
                    etNote.setError("Please enter a note");
                } else {
                    presenter.updateNote(id, title, note, color);
                }
                return true;
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm delete!");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setNegativeButton("Yes", ((dialog, i) -> {
                    dialog.dismiss();
                    presenter.deleteNote(id);
                }));
                alertDialog.setPositiveButton("No", ((dialog, i) -> {
                    dialog.dismiss();
                }));
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private void setDataFromIntentExtra() {
        if (id != null) {
            etTitle.setText(title);
            etNote.setText(note);
            palette.setSelectedColor(color);
            getSupportActionBar().setTitle(UPDATE_NOTE);
            readMode();
        } else {
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    private void editMode() {
        etTitle.setFocusableInTouchMode(true);
        etNote.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }

    private void readMode() {
        etTitle.setFocusableInTouchMode(false);
        etNote.setFocusableInTouchMode(false);
        etTitle.setFocusable(false);
        etNote.setFocusable(false);
        palette.setEnabled(false);
    }
}