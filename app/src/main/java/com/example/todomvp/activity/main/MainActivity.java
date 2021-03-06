package com.example.todomvp.activity.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.todomvp.R;
import com.example.todomvp.activity.editor.EditorActivity;
import com.example.todomvp.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    FloatingActionButton fabAdd;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;

    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvNotes);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, EditorActivity.class));
        });

        presenter = new MainPresenter(this);
        presenter.getData();

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData()
        );

        itemClickListener = (((view, position) -> {
            String id = notes.get(position).getId();
            String title = notes.get(position).getTitle();
            String note = notes.get(position).getNote();
            int color = notes.get(position).getColor();
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("EXTRA_ID", id);
            intent.putExtra("EXTRA_TITLE", title);
            intent.putExtra("EXTRA_NOTE", note);
            intent.putExtra("EXTRA_COLOR", color);
//            startActivityForResult(intent, INTENT_EDIT);
            startActivityForResult.launch(intent);
//            Toast.makeText(this, notes.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        }));

    }

    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    presenter.getData();
                }
            }
    );

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Note> notes) {
        Log.d("data ?", notes.toString());
        adapter = new MainAdapter(notes, this, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        this.notes = notes;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}