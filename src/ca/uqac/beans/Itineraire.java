package ca.uqac.beans;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Itineraire {
    private Long id;
    private Utilisateur conducteur;
    private LocalDate date;
    private LocalTime heure;
    private String destination;
    private String origine;
    private Long nbPlaces;
    private Float prix;
    private String tailleBaggage;
    private Timestamp dateAnnonce;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Utilisateur conducteur) {
        this.conducteur = conducteur;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Long getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Long nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public String getTailleBaggage() {
        return tailleBaggage;
    }

    public void setTailleBaggage(String tailleBaggage) {
        this.tailleBaggage = tailleBaggage;
    }

    public Timestamp getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(Timestamp dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }
}
