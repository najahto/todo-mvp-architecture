package com.example.todomvp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("notes")
    Call<Note> saveNote(
            @Body Note note
    );


}
