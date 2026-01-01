package acp.ferme;

import java.util.Scanner;

/**
 * CLI Interactive pour la Simulation de Ferme.
 * Gère les entrées utilisateur et la boucle de jeu.
 */
public class Main {
    public static void main(String[] args) {
        Ferme ferme = new Ferme("Aux près de Fauriel", 10000);

        // Données intiales
        ferme.ajouterAnimal(new Gallus("Pic"));
        ferme.ajouterAnimal(new Gallus("Poc"));
        ferme.ajouterAnimal(new Gallus("Pac"));

        Scanner scanner = new Scanner(System.in);
        int compteurActions = 0;

        while (true) {
            ferme.afficherApercu();
            System.out.println();
            System.out.println("=========================");
            System.out.println("[1] Acheter une poule (10€)");
            System.out.println("[2] Acheter un mouton (120€)");
            System.out.println("[3] Vendre la production");
            System.out.println("[4] Acheter du foin (10€/kg)");
            System.out.println("[5] Acheter des graines (5€/kg)");
            System.out.println(".........................");
            System.out.println("[6] Jour suivant");
            System.out.println("-------------------------");
            System.out.println("[7] Sauvegarder l'état");
            System.out.println("[8] Charger la sauvegarde");
            System.out.println("[9] Lister les animaux (tri par âge)");
            System.out.println("[0] Quitter");
            System.out.println("=========================");
            System.out.print(ferme.getJour() + " > ");

            String choixStr = scanner.nextLine(); // Evite les pbs de buffer
            int choix = -1;
            try {
                choix = Integer.parseInt(choixStr);
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide.");
                continue;
            }

            switch (choix) {
                case 1: // Poule
                    System.out.print("Nom de la poule : ");
                    String nomPoule = scanner.nextLine();
                    if (nomPoule.isEmpty())
                        nomPoule = "Poule " + (int) (Math.random() * 1000);
                    if (ferme.ajouterAnimal(new Gallus(nomPoule), 10.0)) {
                        compteurActions++;
                    }
                    break;
                case 2: // Mouton
                    System.out.print("Nom du mouton : ");
                    String nomMouton = scanner.nextLine();
                    if (nomMouton.isEmpty())
                        nomMouton = "Mouton " + (int) (Math.random() * 1000);
                    if (ferme.ajouterAnimal(new Ovis(nomMouton), 120.0)) {
                        compteurActions++;
                    }
                    break;
                case 3: // Vente
                    ferme.vendreProduction();
                    compteurActions++;
                    break;
                case 4: // Foin
                    System.out.print("Quantité de foin (kg) : ");
                    try {
                        int qteFoin = Integer.parseInt(scanner.nextLine());
                        ferme.acheterNourriture(Ressource.FOIN, qteFoin, qteFoin * 10.0);
                        compteurActions++;
                    } catch (NumberFormatException e) {
                    }
                    break;
                case 5: // Graines
                    System.out.print("Quantité de graines (kg) : ");
                    try {
                        int qteGraines = Integer.parseInt(scanner.nextLine());
                        ferme.acheterNourriture(Ressource.GRAINES, qteGraines, qteGraines * 5.0);
                        compteurActions++;
                    } catch (NumberFormatException e) {
                    }
                    break;
                case 6: // Jour suivant
                    ferme.jourSuivant();
                    compteurActions = 0;
                    break;
                case 7: // Sauvegarde
                    ferme.sauvegarder();
                    break;
                case 8: // Charge
                    ferme.charger();
                    compteurActions = 0;
                    break;
                case 9: // Export trié
                    System.out.println("--- Export liste d'animaux ---");
                    System.out.println("[1] Tri par âge (défaut)");
                    System.out.println("[2] Tri par nom");
                    System.out.println("[3] Tri par genre");
                    System.out.print("Choix > ");
                    String subChoiceStr = scanner.nextLine();
                    int subChoice = 1;
                    try {
                        subChoice = Integer.parseInt(subChoiceStr);
                    } catch (Exception e) {
                    }

                    switch (subChoice) {
                        case 2:
                            ferme.listerAnimaux(new TriNom(), "nom");
                            break;
                        case 3:
                            ferme.listerAnimaux(new TriGenre(), "genre");
                            break;
                        case 1:
                        default:
                            ferme.listerAnimaux();
                            break;
                    }
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    scanner.close();
                    return;
                default:
                    System.out.println("Choix inconnu.");
            }

            // Auto-avance après 2 actions
            if (compteurActions >= 2) {
                System.out.println("\nDeux actions effectuées. Une nouvelle journée commence...");
                ferme.jourSuivant();
                compteurActions = 0;
                System.out.println("(Appuyez sur Entrée pour continuer)");
                scanner.nextLine();
            }

            System.out.println();
        }
    }
}
