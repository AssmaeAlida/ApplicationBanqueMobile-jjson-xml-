package com.example.webservicefront;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.webservicefront.R;
import com.example.webservicefront.data.api.RetrofitInstance;
import com.example.webservicefront.data.models.Compte;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompteActivity extends Activity {

    private EditText soldeEditText;
    private Spinner typeSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compte);

        // Initialisation des vues
        soldeEditText = findViewById(R.id.soldeEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        saveButton = findViewById(R.id.saveButton);

        // Configuration du Spinner pour le type de compte
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_compte_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Action lors du clic sur le bouton "Sauvegarder"
        saveButton.setOnClickListener(v -> saveCompte());
    }

    // Méthode pour sauvegarder un compte
    private void saveCompte() {
        String solde = soldeEditText.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString();

        // Validation des champs
        if (TextUtils.isEmpty(solde) || TextUtils.isEmpty(type)) {
            Toast.makeText(AddCompteActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création du compte à ajouter
        Compte newCompte = new Compte();
        newCompte.setSolde(Double.parseDouble(solde));

        // Conversion du type de compte en énumération
        if (type.equals("Courant")) {
            newCompte.setType(Compte.TypeCompte.COURANT);
        } else {
            newCompte.setType(Compte.TypeCompte.EPARGNE);
        }

        // Envoi de la requête pour ajouter un compte via Retrofit
        RetrofitInstance.getApi().addCompte(newCompte).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddCompteActivity.this, "Compte ajouté avec succès", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);

                    finish();  // Fermer l'activité après l'ajout du compte
                } else {
                    Toast.makeText(AddCompteActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(AddCompteActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}