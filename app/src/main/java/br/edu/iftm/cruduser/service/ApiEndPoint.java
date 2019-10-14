package br.edu.iftm.cruduser.service;

import java.util.List;

import br.edu.iftm.cruduser.dto.Login;
import br.edu.iftm.cruduser.dto.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiEndPoint {

    @POST("/users")
    Call<User> insertUser(@Body User user);

    @GET("/users")
    Call<List<User>> getAllUsers(@Header("Authorization") String authorization);

    @POST("/auth/login")
    Call<Login> login(@Body Login login);

    @PUT("/users/{id}")
    Call<User> updateUser(@Body User user, @Path("id") Integer id, @Header("Authorization") String authorization);

    @DELETE("/users/{id}")
    Call<Void> deleteUser(@Path("id") Integer id, @Header("Authorization") String authorization);
}
