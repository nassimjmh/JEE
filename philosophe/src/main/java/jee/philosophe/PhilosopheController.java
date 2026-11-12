package jee.philosophe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/philosophe")
public class PhilosopheController {

    @Autowired
    private PhilosopheService philosopheService;

    @Value("${philosopher.id}")
    private int philosopherId;

    @GetMapping("/fourchette")
    public boolean demanderFourchette() {
        return philosopheService.preterFourchette();
    }

    @PostMapping("/rendre-fourchette")
    public void rendreFourchette() {
        philosopheService.recupererFourchette();
    }

    @GetMapping("/etat")
    public String getEtat() {
        return "Le philosophe " + philosopherId + " est actuellement : " + philosopheService.getEtatActuel();
    }

    @PostMapping("/demarrer")
    public String demarrerCycle() {
        // Exécution dans un thread séparé pour ne pas bloquer la requête HTTP
        new Thread(() -> philosopheService.executerCyclePhilosophe()).start();
        return "Cycle démarré pour le philosophe " + philosopherId;
    }
}