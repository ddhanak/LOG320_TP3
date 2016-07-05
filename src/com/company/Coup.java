package com.company;

public class Coup {

    public Coup(Position depart, Position arrivee) {
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public Coup(String coupEnTexte) {
        this.depart = new Position(coupEnTexte.substring(0, 2));
        this.arrivee = new Position(coupEnTexte.substring(5));
    }

    public Position depart;
    public Position arrivee;

    // *IMPORTANT : il faudra vérifier comment le board est construit pour savoir comment faire la string
    @Override
    public String toString() {
        return depart.toString() +" - "+ arrivee.toString();
    }
}
