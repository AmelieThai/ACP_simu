package acp.ferme;

import java.util.Comparator;

public class TriGenre implements Comparator<Animal> {
    @Override
    public int compare(Animal a1, Animal a2) {
        // false (male) < true (femelle) usually with Boolean.compare
        return Boolean.compare(a1.getGenre(), a2.getGenre());
    }
}
