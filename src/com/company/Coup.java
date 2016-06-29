package com.company;

public class Coup {

    public Coup(Position depart, Position arrivee) {
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public Position depart;
    public Position arrivee;

    // Il faudra vérifier comment le board est construit pour savoir comment faire la string
    @Override
    public String toString() {
        return "";
    }

    private char[] lettres = {
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H'
    };
}
