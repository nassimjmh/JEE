package jee.produit_service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/produits")
public class ProduitController {
    // Produit deja défini dans le programme
    // ConcurrentHashMap pour que ça soit modifiable après création
    // Plusieurs requetes en parrallele
    private final Map<Integer, Produit> produits = new ConcurrentHashMap<>(Map.of(
            1, new Produit(1, "Ordinateur", 1200),
            2, new Produit(2, "Souris", 25),
            3, new Produit(3, "Clavier", 60)));

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduit(@PathVariable int id) {
        Produit p = produits.get(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }
}
