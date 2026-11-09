package jee.client_service;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class ClientController {

    private final RestClient restClient = RestClient.create("http://localhost:8080");

    @GetMapping("/client/produit/{id}")
    public Produit getProduit(@PathVariable int id) {
        return restClient.get()
                .uri("/produits/{id}", id)
                .retrieve()
                .body(Produit.class);
    }
}
