package jee.kafka_commandes_service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/commandes")
public class CommandeController {

    // KAFKA Topic = un channel discord qui reçois des msg

    private static final String TOPIC = "commandes-creees";

    // Kafka template format : format clé, format valeur (string = pas besoin de serializer)
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public CommandeController(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    @PostMapping
    public Commande creer(@RequestBody Commande commande) {
        String json = jsonMapper.writeValueAsString(commande);
        kafkaTemplate.send(TOPIC, String.valueOf(commande.client_id()), json);
        return commande;
    }
}
