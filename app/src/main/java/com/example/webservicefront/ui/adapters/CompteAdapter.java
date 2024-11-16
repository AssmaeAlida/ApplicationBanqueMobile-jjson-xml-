package com.example.webservicefront.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.webservicefront.R;
import com.example.webservicefront.data.models.Compte;

import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> comptes;
    private final OnCompteClickListener listener;

    public CompteAdapter(List<Compte> comptes, OnCompteClickListener listener) {
        this.comptes = comptes;
        this.listener = listener;
    }

    @Override
    public CompteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.soldeTextView.setText("Solde: " + compte.getSolde());
        holder.typeTextView.setText("Type: " + compte.getType());

        holder.editIcon.setOnClickListener(v -> listener.onEditClick(compte));
        holder.deleteIcon.setOnClickListener(v -> listener.onDeleteClick(compte));
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }


    public void updateComptes(List<Compte> newComptes) {
        this.comptes.clear();
        this.comptes.addAll(newComptes);
        notifyDataSetChanged();
    }

    public class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView soldeTextView, typeTextView;
        ImageView editIcon, deleteIcon;

        public CompteViewHolder(View itemView) {
            super(itemView);
            soldeTextView = itemView.findViewById(R.id.soldeTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }

    public interface OnCompteClickListener {
        void onEditClick(Compte compte);
        void onDeleteClick(Compte compte);

    }
    public void updateCompte(Compte updatedCompte) {
        // Trouver l'index du compte à mettre à jour
        for (int i = 0; i < comptes.size(); i++) {
            if (comptes.get(i).getId() == updatedCompte.getId()) {
                // Remplacer l'ancien compte par le compte mis à jour
                comptes.set(i, updatedCompte);
                notifyItemChanged(i); // Notifier que l'élément a été modifié
                break;
            }
        }
    }





}
