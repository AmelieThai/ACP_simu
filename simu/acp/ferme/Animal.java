package acp.ferme;

/**
 * Entité abstraite représentant un animal de la ferme.
 * Gère l'âge, la nourriture et le cycle de vie.
 */
public abstract class Animal implements Comparable<Animal> {
    protected String nom;
    protected boolean genre; // true: Femelle, false: Mâle
    protected int[] ages; // [0] actuel, [1] début production, [2] espérance vie
    protected int[] nourriture; // [0] actuelle, [1] besoin journalier, [2] capacité max

    public Animal(String nom, int age, int age_prod, int age_max, int f_init, int f_jour, int f_max, boolean genre) {
        this.nom = nom;
        this.ages = new int[] { age, age_prod, age_max };
        this.nourriture = new int[] { f_init, f_jour, f_max };
        this.genre = genre;
    }

    // Vieillit et consomme la ration journalière
    public void vieillir() {
        ages[0]++;
        nourriture[0] -= nourriture[1];
    }

    // Remplit le stock de nourriture interne jusqu'au max
    public void nourrir(int qte) {
        nourriture[0] += qte;
        if (nourriture[0] > nourriture[2])
            nourriture[0] = nourriture[2];
    }

    // Vérifie la vitalité (faim et âge)
    public boolean estVivant() {
        return (nourriture[0] > 0) && (ages[0] < ages[2]);
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return ages[0];
    }

    public boolean getGenre() {
        return genre;
    }

    public String dump() {
        return String.format("%s | Nourriture: %d/%d| Age: %d (Prod: %d, Max: %d)",
                nom, nourriture[0], nourriture[2], ages[0], ages[1], ages[2]);
    }

    public String toString() {
        int annees = (int) (ages[0] / Utils.JOURS_PAR_AN);
        String genreStr = genre ? "femelle" : "mâle";
        return String.format("%s, %s, %d jours (%d ans)", nom, genreStr, ages[0], annees);
    }

    // Tri par âge (du plus jeune au plus vieux)
    @Override
    public int compareTo(Animal o) {
        return Integer.compare(this.getAge(), o.getAge());
    }

    public abstract void hi();

    public abstract int produire();

    public abstract int getRation();
}
