jdk 21



| Objectif | Commande |
|---|---|
| Lancer l'appli en développement | `.\mvnw.cmd spring-boot:run` |
| Vérifier que ça compile | `.\mvnw.cmd clean compile` |
| Lancer les tests | `.\mvnw.cmd test` |
| Construire le jar (pour Docker) | `.\mvnw.cmd clean package -DskipTests` |
| Build complet avec tests (avant commit ou rendu) | `.\mvnw.cmd clean package` |
| Forcer le re-téléchargement des dépendances | `.\mvnw.cmd -U clean compile` |


Ouvrir sur un autre port :
mvn spring-boot:run -D"spring-boot.run.arguments=--server.port=8081

Image : 
docker build -t hello-service .

Container : 
docker run -d --name hello1 -p 8080:8080 hello-service .


| TP | Service | Dependencies |
|---|---|---|
TP1 | `hello-service` | Spring Web
TP2 | `produit-service`<br> `client-service` | Spring Web


Ajouter un produit : `curl -X POST http://localhost:8080/produits \
     -H "Content-Type: application/json" \
     -d '{"id": 4, "nom": "Telephone", "prix": 800}'`



# Dîner des Philosophes - Version Distribuée (REST)

Requêtes HTTP REST

## Topologie
5 instances sur les ports `8081` à `8085` (`ID` de 1 à 5) :
- **Voisin de droite :** `(ID % 5) + 1`
- **Voisin de gauche :** `((ID - 2 + 5) % 5) + 1`

## Cycle de vie d'un philosophe

Chaque philosophe exécute une boucle jusqu'à avoir mangé **3 repas** :

1. **RÉFLÉCHIR** : Le philosophe pense pendant une durée aléatoire (2 à 4s) pour désynchroniser les demandes sur le réseau.
2. **AFFAMÉ** : Il tente d'acquérir les fourchettes de ses deux voisins :
    - Il demande la fourchette de **droite** (`GET /philosophe/fourchette`).
    - Si elle est obtenue, il demande la fourchette de **gauche**.
    - **Repli (Anti-Deadlock)** : Si la fourchette gauche est refusée, il rend immédiatement la fourchette droite (`POST /philosophe/rendre-fourchette`) et patiente 1.5s avant de recommencer.
3. **MANGER** : S'il possède les deux fourchettes, il mange pendant 3s, puis libère les deux fourchettes auprès de ses voisins.
4. **FIN** : Après 3 repas, le philosophe quitte le cycle définitivement.

## Endpoints REST exposés
- `POST /philosophe/demarrer` : Lance le cycle de vie du philosophe en arrière-plan.
- `GET /philosophe/fourchette` : Permet à un voisin d'emprunter la fourchette locale (méthode `synchronized`).
- `POST /philosophe/rendre-fourchette` : Permet à un voisin de restituer la fourchette locale.
- `GET /philosophe/etat` : Affiche l'état courant du philosophe.


# TP3 KAFKA

docker compose up -d
   