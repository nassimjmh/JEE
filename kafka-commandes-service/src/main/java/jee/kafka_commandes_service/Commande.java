package jee.kafka_commandes_service;

public record Commande(int commande_id, int client_id, String produit, double montant, String date) {}
