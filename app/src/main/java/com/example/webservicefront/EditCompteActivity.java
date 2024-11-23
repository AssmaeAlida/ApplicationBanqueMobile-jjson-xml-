package com.example.webservicefront;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webservicefront.data.api.CompteApi;
import com.example.webservicefront.data.api.Config;
import com.example.webservicefront.data.models.Compte;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditCompteActivity extends AppCompatActivity {
    private EditText editSolde;
    private Spinner editType;
    private Button saveButton;
    private Button cancelButton;
    private Long compteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_compte);


        editSolde = findViewById(R.id.editSolde);
        editType = findViewById(R.id.editType);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);


        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"COURANT", "EPARGNE"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editType.setAdapter(typeAdapter);


        compteId = getIntent().getLongExtra("compte_id", -1);

        if (compteId != -1) {
            fetchCompteDetails();
        }

        saveButton.setOnClickListener(v -> saveCompte());
        cancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void fetchCompteDetails() {
        CompteApi api = Config.getClient("application/json").create(CompteApi.class);
        Call<Compte> call = api.getCompte(compteId);

        call.enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Compte compte = response.body();
                    editSolde.setText(String.valueOf(compte.getSolde()));
                    // Définir la sélection du type dans le Spinner
                    String type = compte.getType();
                    if (type != null) {
                        int position = type.equals("COURANT") ? 0 : 1;
                        editType.setSelection(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(EditCompteActivity.this, "Erreur lors de la récupération", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCompte() {
        try {
            double solde = Double.parseDouble(editSolde.getText().toString());
            String type = editType.getSelectedItem().toString(); // Obtenir le type sélectionné dans le Spinner

            Compte updatedCompte = new Compte(compteId, solde, type);

            CompteApi api = Config.getClient("application/json").create(CompteApi.class);
            Call<Compte> call = api.updateCompte(compteId, updatedCompte);

            call.enqueue(new Callback<Compte>() {
                @Override
                public void onResponse(Call<Compte> call, Response<Compte> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditCompteActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditCompteActivity.this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Compte> call, Throwable t) {
                    Toast.makeText(EditCompteActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un solde valide", Toast.LENGTH_SHORT).show();
        }
    }
}