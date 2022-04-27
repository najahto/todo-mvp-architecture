package com.example.todomvp.activity.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todomvp.api.ApiClient;
import com.example.todomvp.api.ApiInterface;
import com.example.todomvp.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    void getData() {
        view.showLoading();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Note>> call = apiInterface.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                view.hideLoading();
                Log.d("Err ??", t.getMessage());
                Log.d("Err ??", t.getStackTrace().toString());
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

}
