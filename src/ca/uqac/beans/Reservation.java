package ca.uqac.beans;

import java.sql.Timestamp;

public class Reservation {
    private Long id;
    private Utilisateur client;
    private Itineraire itineraire;
    private Long nbSieges;
    private Timestamp dateReservation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur getClient() {
        return client;
    }

    public void setClient(Utilisateur client) {
        this.client = client;
    }

    public Itineraire getItineraire() {
        return itineraire;
    }

    public void setItineraire(Itineraire itineraire) {
        this.itineraire = itineraire;
    }

    public Long getNbSieges() {
        return nbSieges;
    }

    public void setNbSieges(Long nbSieges) {
        this.nbSieges = nbSieges;
    }

    public Timestamp getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }
}
