package com.example.webservicefront.data.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import java.io.Serializable;
import java.util.Date;

@Root(name = "compte", strict = false)  // Indique que cette classe représente un élément XML appelé "compte"
public class Compte implements Serializable {

    @Element(name = "id")
    private Long id;

    @Element(name = "solde")
    private double solde;

    @Element(name = "dateCreation")
    private Date dateCreation;

    private String type;

    public Compte(Long id, double solde, String type) {
        this.id = id;
        this.solde = solde;
        this.type = type;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Enum pour TypeCompte
    public enum TypeCompte {
        @Element(name = "courant")
        COURANT,

        @Element(name = "epargne")
        EPARGNE
    }
}
