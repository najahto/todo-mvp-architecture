package com.example.todomvp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {

    EditText etTitle, etNote;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        etTitle = findViewById(R.id.etTitle);
        etNote = findViewById(R.id.etNote);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                // save
                String title = etTitle.getText().toString().trim();
                String note = etNote.getText().toString().trim();
                int color = 35453;

                if (title.isEmpty()) {
                    etTitle.setError("Please enter a title ");
                } else if (note.isEmpty()) {
                    etNote.setError("Please enter a note");
                } else {
                    saveNote(title, note, color);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveNote(final String title, final String note, final int color) {
        progressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Note newNote = new Note(title, note);
        Call<Note> call = apiInterface.saveNote(newNote);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        Log.d("message s?", response.body().getMessage());
                        Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); // back to the main activity
                    } else {
                        Log.d("message e?", response.body().getMessage());
                        Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("message error?", t.getLocalizedMessage());
                Toast.makeText(EditorActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}