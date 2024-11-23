package com.example.webservicefront.data.api;

import com.example.webservicefront.data.models.Compte;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CompteApi {

    // Récupérer la liste des comptes (avec l'en-tête Accept pour JSON ou XML)
    @GET("comptes")
    Call<List<Compte>> getComptes();

    @GET("comptes/{id}")
    Call<Compte> getCompte(@Path("id") Long id);

    @POST("comptes")
    Call<Compte> addCompte(@Body Compte compte);

    @PUT("comptes/{id}")
    Call<Compte> updateCompte(@Path("id") Long id, @Body Compte compte);

    @DELETE("comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id);
}
