package acp.ferme;

/**
 * Implémentation de la Poule (Gallus).
 * Produit des oeufs (1/jour) si femelle et mature. Consomme des graines.
 */
public class Gallus extends Animal {
    private static final int AGE_MAX = (int) (10 * Utils.JOURS_PAR_AN);
    private static final int NOURRITURE_MAX = 400;
    private static final int RATION_JOUR = 120;

    private boolean aProduitAujourdhui = false;

    public Gallus(String nom, int age, int age_prod, int age_max, int nourriture, int nourriture_jour,
            int nourriture_max, boolean genre) {
        super(nom, age, age_prod, age_max, nourriture, nourriture_jour, nourriture_max, genre);
    }

    public Gallus(String nom, int age, boolean genre) {
        super(nom, age,
                (int) Math.round(7 * 30 + Utils.rng.nextGaussian()),
                (int) Math.round(AGE_MAX + Utils.rng.nextGaussian()),
                0, RATION_JOUR, NOURRITURE_MAX, genre);
    }

    public Gallus(String nom) {
        this(nom, (int) Math.round(2 * Utils.JOURS_PAR_AN + Utils.rng.nextGaussian()), Utils.rng.nextBoolean());
    }

    @Override
    public void vieillir() {
        super.vieillir();
        aProduitAujourdhui = false; // Réinitialise l'état de production journalier
    }

    public void hi() {
        if (!estVivant())
            return;
        System.out.println(nom + (genre ? " : Côt côt" : " : Cocorrico"));
    }

    // Retourne la quantité d'oeufs produits (0 ou 1)
    public int produire() {
        if (!estVivant())
            return 0;
        if (genre && ages[0] >= ages[1] && !aProduitAujourdhui) {
            if (Utils.rng.nextBoolean()) {
                aProduitAujourdhui = true;
                return 1;
            }
        }
        return 0;
    }

    public int getRation() {
        return nourriture[1]; // Utilise le champ protégé renommé
    }

    @Override
    public String dump() {
        return "Gallus " + super.dump();
    }

    @Override
    public String toString() {
        return "Gallus " + super.toString();
    }

    public static void main(String[] args) {
        System.out.println("--- Test Gallus (Refactored) ---");
        Gallus g1 = new Gallus("Poc");
        System.out.println(g1);
        g1.hi();
    }
}
