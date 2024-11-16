package com.example.webservicefront;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webservicefront.data.api.RetrofitInstance;
import com.example.webservicefront.data.models.Compte;
import com.example.webservicefront.ui.adapters.CompteAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private Button addButton;

    private static final int ADD_COMPTE_REQUEST_CODE = 1;
    private static final int EDIT_COMPTE_REQUEST_CODE = 2; // Ajouté pour l'édition

    private List<Compte> comptes;
    private RadioGroup formatRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation du bouton Ajouter un compte
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // Ouvrir une nouvelle activité pour ajouter un compte
            Intent intent = new Intent(MainActivity.this, AddCompteActivity.class);
            startActivityForResult(intent, ADD_COMPTE_REQUEST_CODE);
        });

        // Appel Retrofit pour récupérer la liste des comptes
        RetrofitInstance.getApi().getComptes().enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful()) {
                    List<Compte> comptes = response.body();
                    // Initialiser l'adapter avec la liste des comptes
                    adapter = new CompteAdapter(comptes, new CompteAdapter.OnCompteClickListener() {
                        @Override
                        public void onEditClick(Compte compte) {
                            // Lancer l'activité d'édition
                            Intent intent = new Intent(MainActivity.this, EditCompteActivity.class);
                            intent.putExtra("compte", compte); // Passer le compte à l'activité d'édition
                            startActivityForResult(intent, 1); // Demander un retour (si besoin de récupérer les données modifiées)
                        }

                        @Override
                        public void onDeleteClick(Compte compte) {
                            // Logique pour supprimer un compte
                            deleteCompte(compte);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Error: " + response.code());
                }
            }

        @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Méthode pour supprimer un compte
    private void deleteCompte(Compte compte) {
        RetrofitInstance.getApi().deleteCompte(compte.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte supprimé", Toast.LENGTH_SHORT).show();
                    // Recharger la liste des comptes après la suppression
                    reloadComptes();
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Méthode pour recharger les comptes après modification ou suppression
    private void reloadComptes() {
        RetrofitInstance.getApi().getComptes().enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful()) {
                    List<Compte> comptes = response.body();
                    if (adapter == null) {
                        adapter = new CompteAdapter(comptes, new CompteAdapter.OnCompteClickListener() {
                            @Override
                            public void onEditClick(Compte compte) {
                                Intent intent = new Intent(MainActivity.this, EditCompteActivity.class);
                                intent.putExtra("compte", compte);
                                startActivityForResult(intent, EDIT_COMPTE_REQUEST_CODE);
                            }

                            @Override
                            public void onDeleteClick(Compte compte) {
                                deleteCompte(compte);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateComptes(comptes); // Mettre à jour l'adaptateur
                    }
                } else {
                    Log.e("MainActivity", "Erreur lors du rechargement des comptes : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Log.e("MainActivity", "Erreur lors du rechargement des comptes : " + t.getMessage());
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_COMPTE_REQUEST_CODE && resultCode == RESULT_OK) {
            reloadComptes(); // Recharge la liste des comptes après l'ajout
        } else if (requestCode == EDIT_COMPTE_REQUEST_CODE && resultCode == RESULT_OK) {
            reloadComptes(); // Recharge la liste des comptes après la modification
        }
    }











}
