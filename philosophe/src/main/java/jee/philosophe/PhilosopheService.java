package jee.philosophe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PhilosopheService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${philosopher.id}")
    private int philosopherId;

    private String etatActuel = "RÉFLÉCHIR";
    private boolean fourchetteDisponible = true;
 
    // Synchronized : un seul thread à la fois
    public synchronized boolean preterFourchette() {
        if (fourchetteDisponible && !"MANGER".equals(etatActuel)) {
            fourchetteDisponible = false;
            System.out.println("[PHILOSOPHE " + philosopherId + "] Je prête ma fourchette à un voisin.");
            return true;
        }
        System.out.println("[PHILOSOPHE " + philosopherId + "] Refus de prêter ma fourchette (occupée ou je mange).");
        return false;
    }

    // Synchronized : un seul thread à la fois
    public synchronized void recupererFourchette() {
        fourchetteDisponible = true;
        System.out.println("[PHILOSOPHE " + philosopherId + "] Ma fourchette a été rendue.");
    }

    public String getEtatActuel() {
        return etatActuel;
    }


    public void executerCyclePhilosophe() {
        int repasManges = 0;
        int maxRepas = 3;

        while (repasManges < maxRepas) {
            try {
                changerEtat("RÉFLÉCHIR");
                Thread.sleep(2000 + (long) (Math.random() * 2000)); // Temps aléatoire pour éviter la synchronisation parfaite

                changerEtat("AFFAMÉ");

                if (acquerirFourchettes()) {
                    changerEtat("MANGER");
                    Thread.sleep(3000);

                    // Libérer les deux fourchettes une fois le repas terminé
                    libererFourchettes();

                    repasManges++;
                    System.out.println("[PHILOSOPHE " + philosopherId + "] Repas terminés : " + repasManges + "/" + maxRepas);
                } else {
                    System.out.println("[PHILOSOPHE " + philosopherId + "] Fourchettes indisponibles. Je patiente...");
                    Thread.sleep(1500);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Le cycle du philosophe " + philosopherId + " a été interrompu.");
                break;
            }
        }

        changerEtat("FIN (Retiré du service définitivement)");
    }

    private void changerEtat(String nouvelEtat) {
        this.etatActuel = nouvelEtat;
        System.out.println("[PHILOSOPHE " + philosopherId + "] État actuel : " + etatActuel);
    }


    private String getUrlVoisinDroite() {
        int idVoisinDroite = (philosopherId % 5) + 1;
        return "http://localhost:" + (8080 + idVoisinDroite) + "/philosophe";
    }

    private String getUrlVoisinGauche() {
        int idVoisinGauche = ((philosopherId - 2 + 5) % 5) + 1;
        return "http://localhost:" + (8080 + idVoisinGauche) + "/philosophe";
    }

    public boolean acquerirFourchettes() {
        String urlDroite = getUrlVoisinDroite();
        String urlGauche = getUrlVoisinGauche();

        boolean aDroite = demanderUneFourchette(urlDroite);
        if (!aDroite) {
            return false;
        }

        boolean aGauche = demanderUneFourchette(urlGauche);
        if (!aGauche) {
            // Repli : on rend immédiatement la fourchette droite si la gauche est prise pr pas ostage une seule fourchette
            rendreUneFourchette(urlDroite);
            return false;
        }

        return true;
    }

    public void libererFourchettes() {
        rendreUneFourchette(getUrlVoisinDroite());
        rendreUneFourchette(getUrlVoisinGauche());
    }

    private boolean demanderUneFourchette(String baseUrl) {
        try {
            Boolean reponse = restTemplate.getForObject(baseUrl + "/fourchette", Boolean.class);
            return Boolean.TRUE.equals(reponse);
        } catch (Exception e) {
            System.err.println("[PHILOSOPHE " + philosopherId + "] Impossible de contacter " + baseUrl);
            return false;
        }
    }

    private void rendreUneFourchette(String baseUrl) {
        try {
            restTemplate.postForObject(baseUrl + "/rendre-fourchette", null, Void.class);
        } catch (Exception e) {
            System.err.println("[PHILOSOPHE " + philosopherId + "] Erreur lors de la restitution à " + baseUrl);
        }
    }

    // Lance le cycle automatiquement sans curl
    @EventListener(ApplicationReadyEvent.class)
    public void demarrerAutomatiquement() {
        new Thread(() -> {
            try {
                Thread.sleep(15000);
                System.out.println("[PHILOSOPHE " + philosopherId + "] Démarrage automatique du cycle !");
                executerCyclePhilosophe();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }



}