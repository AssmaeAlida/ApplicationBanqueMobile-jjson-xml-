package com.example.webservicefront;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.webservicefront.data.api.RetrofitInstance;
import com.example.webservicefront.data.models.Compte;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCompteActivity extends Activity {

    private EditText soldeEditText;
    private Spinner typeSpinner;
    private Button saveButton;
    private Compte compteToEdit;
    private ImageView editIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_compte);

        // Récupérer l'objet compte passé par l'intent
        compteToEdit = (Compte) getIntent().getSerializableExtra("compte");

        soldeEditText = findViewById(R.id.soldeEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        saveButton = findViewById(R.id.saveButton);
        editIcon = findViewById(R.id.editIcon);

        // Initialiser le Spinner avec les types de comptes
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_compte_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Pré-remplir les champs avec les valeurs actuelles
        if (compteToEdit != null) {
            soldeEditText.setText(String.valueOf(compteToEdit.getSolde()));

            // Pré-sélectionner le type de compte dans le spinner
            if (compteToEdit.getType() != null) {
                if (compteToEdit.getType() == Compte.TypeCompte.COURANT) {
                    typeSpinner.setSelection(0);  // Sélectionner "COURANT"
                } else if (compteToEdit.getType() == Compte.TypeCompte.EPARGNE) {
                    typeSpinner.setSelection(1);  // Sélectionner "EPARGNE"
                }
            }
        }

        // Icône pour modifier
        editIcon.setOnClickListener(v -> {
            Toast.makeText(EditCompteActivity.this, "Vous êtes déjà dans l'activité de modification", Toast.LENGTH_SHORT).show();
        });

        // Bouton pour sauvegarder les modifications
        saveButton.setOnClickListener(v -> {
            String newSolde = soldeEditText.getText().toString();

            // Assurez-vous que le solde n'est pas vide et est un nombre valide
            if (!newSolde.isEmpty()) {
                try {
                    double parsedSolde = Double.parseDouble(newSolde);
                    compteToEdit.setSolde(parsedSolde);

                    // Vérifier que le spinner n'est pas nul et obtenir la valeur sélectionnée
                    String newType = "";
                    if (typeSpinner.getSelectedItem() != null) {
                        newType = typeSpinner.getSelectedItem().toString();
                    }

                    if (!newType.isEmpty()) {
                        // Mettre à jour le type en fonction de la sélection du spinner
                        if (newType.equals("COURANT")) {
                            compteToEdit.setType(Compte.TypeCompte.COURANT);
                        } else if (newType.equals("EPARGNE")) {
                            compteToEdit.setType(Compte.TypeCompte.EPARGNE);
                        }
                    } else {
                        Toast.makeText(EditCompteActivity.this, "Le type de compte doit être sélectionné", Toast.LENGTH_SHORT).show();
                        return;  // Empêcher la sauvegarde si le type est vide
                    }

                    // Envoyer les données du compte à l'API
                    RetrofitInstance.getApi().updateCompte(compteToEdit.getId(), compteToEdit).enqueue(new Callback<Compte>() {
                        @Override
                        public void onResponse(Call<Compte> call, Response<Compte> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                compteToEdit = response.body(); // Mettre à jour l'objet avec la réponse du serveur
                                Toast.makeText(EditCompteActivity.this, "Compte modifié avec succès", Toast.LENGTH_SHORT).show();

                                // Retourner le compte mis à jour à MainActivity
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("updatedCompte", compteToEdit); // Passer l'objet modifié
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                Toast.makeText(EditCompteActivity.this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
                            }
                        }



                        @Override
                        public void onFailure(Call<Compte> call, Throwable t) {
                            Toast.makeText(EditCompteActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NumberFormatException e) {
                    Toast.makeText(EditCompteActivity.this, "Le solde doit être un nombre valide", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditCompteActivity.this, "Le solde ne peut pas être vide", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
