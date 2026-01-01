package acp.ferme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

/**
 * Logique principale de gestion de la ferme.
 * Gère les actifs (animaux, stocks, capital), la persistance (CSV) et le cycle
 * de simulation (jourSuivant).
 */
public class Ferme {
    private String nom;
    private int jour;
    private double capital;
    private ArrayList<Animal> animaux;
    private HashMap<Ressource, Integer> stocks;
    private double prixLait;
    private double prixOeuf;

    public Ferme(String nom, double capital) {
        this.nom = nom;
        this.capital = capital;
        this.jour = 0;
        this.animaux = new ArrayList<>();
        this.stocks = new HashMap<>();

        // Stocks par défaut
        stocks.put(Ressource.OEUFS, 0);
        stocks.put(Ressource.LAIT, 0);
        stocks.put(Ressource.GRAINES, 10000);
        stocks.put(Ressource.FOIN, 1000000);

        this.prixLait = 1.5;
        this.prixOeuf = 0.95;
    }

    public Ferme(String nom) {
        this(nom, 10000.0);
    }

    public int getJour() {
        return jour;
    }

    public void setPrixLait(double p) {
        this.prixLait = p;
    }

    public void setPrixOeuf(double p) {
        this.prixOeuf = p;
    }

    public boolean ajouterAnimal(Animal a, double prix) {
        if (capital >= prix) {
            animaux.add(a);
            capital -= prix;
            System.out.println("Achat de " + a.getNom() + " pour " + prix + "€. Capital restant : " + capital + "€");
            return true;
        } else {
            System.out.println("Fonds insuffisants pour acheter " + a.getNom());
            return false;
        }
    }

    public boolean ajouterAnimal(Animal a) {
        animaux.add(a);
        System.out.println("Ajout de " + a.getNom() + " au cheptel (gratuit).");
        return true;
    }

    public void supprimerAnimal(Animal a) {
        if (animaux.remove(a)) {
            System.out.println(a.getNom() + " a quitté la ferme.");
        } else {
            System.out.println(a.getNom() + " n'était pas dans la ferme.");
        }
    }

    public void acheterNourriture(Ressource n, int qte, double prix) {
        if (capital >= prix) {
            stocks.put(n, stocks.getOrDefault(n, 0) + qte);
            capital -= prix;
            System.out.println(
                    "Achat de " + qte + " unités de " + n + " pour " + prix + "€. Capital restant : " + capital + "€");
        } else {
            System.out.println("Fonds insuffisants pour acheter de la nourriture.");
        }
    }

    public void vendreProduction() {
        int qteOeufs = stocks.getOrDefault(Ressource.OEUFS, 0);
        int qteLait = stocks.getOrDefault(Ressource.LAIT, 0);

        double gain = qteOeufs * prixOeuf + qteLait * prixLait;
        capital += gain;

        stocks.put(Ressource.OEUFS, 0);
        stocks.put(Ressource.LAIT, 0);

        if (gain > 0) {
            System.out.println("Vente de la production effectuée. Gain : " + String.format("%.2f", gain)
                    + "€. Nouveau capital : " + String.format("%.2f", capital) + "€");
        } else {
            System.out.println("Rien à vendre aujourd'hui.");
        }
    }

    public void afficherAnimaux() {
        if (animaux.isEmpty()) {
            System.out.println("La ferme ne possède pas encore d'animaux.");
            return;
        }
        for (Animal a : animaux) {
            System.out.println(a);
        }
    }

    public String toString() {
        return String.format("%s (jour %d) %.0f€ |Oeufs : %d |Lait : %dl |Foin : %dkg |Graines : %dkg",
                nom,
                jour,
                capital,
                stocks.getOrDefault(Ressource.OEUFS, 0),
                stocks.getOrDefault(Ressource.LAIT, 0),
                stocks.getOrDefault(Ressource.FOIN, 0),
                stocks.getOrDefault(Ressource.GRAINES, 0));
    }

    // Simulation d'une journée : Vieillissement, Nourrissage, Production, Décès.
    public void jourSuivant() {
        jour++;
        ListIterator<Animal> it = animaux.listIterator();

        while (it.hasNext()) {
            Animal a = it.next();
            a.vieillir();

            // 1. Identifier type nourriture
            Ressource typeNourriture = (a instanceof Gallus) ? Ressource.GRAINES
                    : (a instanceof Ovis) ? Ressource.FOIN : null;

            // 2. Nourrir animal
            if (typeNourriture != null) {
                int rationDemandee = a.getRation();
                int stockDispo = stocks.getOrDefault(typeNourriture, 0);
                int aDonner = Math.min(rationDemandee, stockDispo);

                stocks.put(typeNourriture, stockDispo - aDonner);
                a.nourrir(aDonner);
            }

            // 3. Récolter production
            int prod = a.produire();
            if (prod > 0) {
                if (a instanceof Gallus)
                    stocks.put(Ressource.OEUFS, stocks.getOrDefault(Ressource.OEUFS, 0) + prod);
                else if (a instanceof Ovis)
                    stocks.put(Ressource.LAIT, stocks.getOrDefault(Ressource.LAIT, 0) + prod);
            }

            // 4. Retirer animaux morts
            if (!a.estVivant()) {
                it.remove();
                System.out.println("  -> " + a.getNom() + " a été retiré du cheptel.");
            }
        }
    }

    // --- Persistance (CSV) ---
    // Sauvegarde l'état dans : fermes.csv (stats) et animaux.csv (cheptel)
    public void sauvegarder() {
        try (PrintWriter pw = new PrintWriter(new File("fermes.csv"))) {
            // Format: nom:jour:capital:stock_graines:stock_foin:stock_oeufs:stock_lait
            pw.printf("%s:%d:%.2f:%d:%d:%d:%d%n",
                    nom, jour, capital,
                    stocks.getOrDefault(Ressource.GRAINES, 0),
                    stocks.getOrDefault(Ressource.FOIN, 0),
                    stocks.getOrDefault(Ressource.OEUFS, 0),
                    stocks.getOrDefault(Ressource.LAIT, 0));
            System.out.println("Sauvegarde de l'état de la ferme terminée.");
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde ferme : " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new File("animaux.csv"))) {
            for (Animal a : animaux) {
                // Format:
                // type:nom:genre(1/0):age:age_prod:age_max:nourriture:ration:max_nourriture
                pw.printf("%s:%s:%d:%d:%d:%d:%d:%d:%d%n",
                        (a instanceof Gallus) ? "Gallus" : "Ovis",
                        a.nom, (a.genre ? 1 : 0),
                        a.ages[0], a.ages[1], a.ages[2],
                        a.nourriture[0], a.nourriture[1], a.nourriture[2]);
            }
            System.out.println("Sauvegarde des animaux terminée.");
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde animaux : " + e.getMessage());
        }
    }

    // Chargement de l'état depuis CSV
    public void charger() {
        // 1. Charger stats ferme
        try (Scanner sc = new Scanner(new File("fermes.csv"))) {
            if (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(":");
                if (parts.length >= 7) {
                    this.nom = parts[0];
                    this.jour = Integer.parseInt(parts[1]);
                    this.capital = Double.parseDouble(parts[2].replace(',', '.'));
                    stocks.put(Ressource.GRAINES, Integer.parseInt(parts[3]));
                    stocks.put(Ressource.FOIN, Integer.parseInt(parts[4]));
                    stocks.put(Ressource.OEUFS, Integer.parseInt(parts[5]));
                    stocks.put(Ressource.LAIT, Integer.parseInt(parts[6]));
                }
            }
            System.out.println("Ferme chargée.");
        } catch (Exception e) {
            System.out.println("Erreur charge ferme (ou fichier absent) : " + e.getMessage());
        }

        // 2. Charger animaux
        try (Scanner sc = new Scanner(new File("animaux.csv"))) {
            animaux.clear();
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(":");
                if (parts.length >= 9) {
                    String type = parts[0];
                    String nom = parts[1];
                    boolean genre = Integer.parseInt(parts[2]) == 1;
                    Animal a = type.equals("Gallus")
                            ? new Gallus(nom, Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                                    Integer.parseInt(parts[5]), Integer.parseInt(parts[6]), Integer.parseInt(parts[7]),
                                    Integer.parseInt(parts[8]), genre)
                            : (type.equals("Ovis")
                                    ? new Ovis(nom, Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                                            Integer.parseInt(parts[5]), Integer.parseInt(parts[6]),
                                            Integer.parseInt(parts[7]), Integer.parseInt(parts[8]), genre)
                                    : null);

                    if (a != null)
                        animaux.add(a);
                }
            }
            System.out.println("Animaux chargés (" + animaux.size() + ").");
        } catch (Exception e) {
            System.out.println("Erreur charge animaux : " + e.getMessage());
        }
    }

    public void afficherApercu() {
        System.out.println(this.toString());

        int nbPoules = 0;
        int nbCoqs = 0;
        int nbMoutons = 0;
        int nbBeliers = 0;

        for (Animal a : animaux) {
            if (a instanceof Gallus) {
                nbPoules++;
                if (!a.genre)
                    nbCoqs++;
            } else if (a instanceof Ovis) {
                nbMoutons++;
                if (!a.genre)
                    nbBeliers++;
            }
        }
        // Format: Moutons : 15 (6 béliers) |Poules : 40 (1 coq)
        // Accord grammatical simple (singulier/pluriel) non géré ici pour simplifier,
        // ou on peut faire un ternaire comme suggéré.
        String strPoules = String.format("Poules : %d (%d coq%s)", nbPoules, nbCoqs, nbCoqs > 1 ? "s" : "");
        String strMoutons = String.format("Moutons : %d (%d bélier%s)", nbMoutons, nbBeliers, nbBeliers > 1 ? "s" : "");

        System.out.println(strMoutons + " | " + strPoules);
    }

    public void listerAnimaux() {
        // Par défaut, tri naturel (Comparable -> par âge)
        listerAnimaux(null, "");
    }

    public void listerAnimaux(java.util.Comparator<Animal> comp, String suffixe) {
        // Créer une copie pour le tri
        ArrayList<Animal> tri = new ArrayList<>(animaux);
        if (comp != null) {
            Collections.sort(tri, comp);
        } else {
            Collections.sort(tri); // Tri naturel
        }

        String filename = "liste_animaux_jour_" + jour + (suffixe.isEmpty() ? "" : "_" + suffixe) + ".csv";

        try (PrintWriter pw = new PrintWriter(new File(filename))) {
            pw.println("Type,Nom,Age (jours),Genre");
            for (Animal a : tri) {
                String type = (a instanceof Gallus) ? "Gallus" : "Ovis";
                String genreNom = "";
                if (a instanceof Gallus) {
                    genreNom = a.getGenre() ? "poule" : "coq";
                } else if (a instanceof Ovis) {
                    genreNom = a.getGenre() ? "brebis" : "bélier";
                }

                pw.printf("%s,%s,%d,%s%n", type, a.getNom(), a.getAge(), genreNom);
            }
            System.out.println("Liste triée exportée dans " + filename);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'export des animaux.");
            e.printStackTrace();
        }
    }

    // Test rapide
    public static void main(String[] args) {
        Ferme f = new Ferme("Ma Belle Ferme");

        Gallus g = new Gallus("Cocotte", 40, false);
        f.ajouterAnimal(g, 100);
        f.acheterNourriture(Ressource.GRAINES, 500, 50);

        System.out.println(" Etat initial : " + f);
        f.sauvegarder();

        System.out.println("--- Reset de la ferme ---");
        f = new Ferme("Vide");
        System.out.println(" Etat vide : " + f);

        System.out.println("--- Chargement ---");
        f.charger();
        System.out.println(" Etat chargé : " + f);
        f.afficherAnimaux();
    }
}
