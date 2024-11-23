package com.example.webservicefront.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webservicefront.EditCompteActivity;
import com.example.webservicefront.R;
import com.example.webservicefront.data.api.CompteApi;
import com.example.webservicefront.data.api.Config;
import com.example.webservicefront.data.models.Compte;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.ViewHolder> {
    private Context context;
    private List<Compte> comptes;

    public CompteAdapter(Context context, List<Compte> comptes) {
        this.context = context;
        this.comptes = comptes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.compte_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compte compte = comptes.get(position);

        holder.solde.setText("Solde: " + compte.getSolde());
        holder.type.setText("Type: " + compte.getType());

        // Edit button
        holder.editIcon.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCompteActivity.class);
            intent.putExtra("compte_id", compte.getId());
            context.startActivity(intent);
        });

        // Delete button
        holder.deleteIcon.setOnClickListener(v -> deleteCompte(compte.getId(), position));
    }

    private void deleteCompte(Long id, int position) {
        String format = "application/json";

        CompteApi api = Config.getClient(format).create(CompteApi.class);
        Call<Void> call = api.deleteCompte(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Supprimer l'élément de la liste si l'API retourne un succès
                    comptes.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Compte supprimé avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Échec de la requête : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView solde, type;
        ImageView editIcon, deleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            solde = itemView.findViewById(R.id.solde);
            type = itemView.findViewById(R.id.type);
            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}