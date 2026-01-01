package acp.ferme;

/**
 * Implémentation du Mouton (Ovis).
 * Produit du lait (qté variable) si femelle et mature. Consomme du foin.
 */
public class Ovis extends Animal {
    private static final int AGE_MAX = (int) (20 * Utils.JOURS_PAR_AN);
    private static final int NOURRITURE_MAX = 15000;
    private static final int RATION_JOUR = 4000;

    public Ovis(String nom, int age, int age_prod, int age_max, int nourriture, int nourriture_jour, int nourriture_max,
            boolean genre) {
        super(nom, age, age_prod, age_max, nourriture, nourriture_jour, nourriture_max, genre);
    }

    public Ovis(String nom, int age, boolean genre) {
        super(nom, age,
                (int) Math.round(Utils.JOURS_PAR_AN + Utils.rng.nextGaussian()),
                (int) Math.round(AGE_MAX + Utils.rng.nextGaussian()),
                0, RATION_JOUR, NOURRITURE_MAX, genre);
    }

    public Ovis(String nom) {
        this(nom, (int) Math.round(2 * Utils.JOURS_PAR_AN + Utils.rng.nextGaussian()), Utils.rng.nextBoolean());
    }

    public void hi() {
        if (estVivant())
            System.out.println(nom + " : Bêêêêêh !");
    }

    // Retourne la quantité de lait produite (en litres)
    public int produire() {
        if (!estVivant())
            return 0;
        if (genre && ages[0] >= ages[1]) {
            return Math.max(0, (int) Math.round(1.0 + 0.5 * Utils.rng.nextGaussian()));
        }
        return 0;
    }

    public int getRation() {
        return nourriture[1];
    }

    @Override
    public String dump() {
        return "Ovis " + super.dump();
    }

    @Override
    public String toString() {
        return "Ovis " + super.toString();
    }

    public static void main(String[] args) {
        System.out.println("--- Test Ovis (Refactored) ---");
        Ovis o1 = new Ovis("Dolly");
        System.out.println(o1);
        o1.hi();
    }
}
