package com.example.todomvp.api;

import com.example.todomvp.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {


    @POST("notes")
    Call<Note> saveNote(
            @Body Note note
    );

    @GET("notes")
    Call<List<Note>> getNotes();

    @PUT("notes/{id}")
    Call<Note> updateNote(
            @Path("id") String id,
            @Body Note note
    );


}
