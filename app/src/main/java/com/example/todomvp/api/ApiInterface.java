package com.example.todomvp.api;

import com.example.todomvp.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("notes")
    Call<Note> saveNote(
            @Body Note note
    );

    @GET("notes")
    Call<List<Note>> getNotes();



}
