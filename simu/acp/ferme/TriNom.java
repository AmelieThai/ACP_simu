package acp.ferme;

import java.util.Comparator;

public class TriNom implements Comparator<Animal> {
    @Override
    public int compare(Animal a1, Animal a2) {
        return a1.getNom().compareTo(a2.getNom());
    }
}
