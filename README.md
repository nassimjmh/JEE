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

TP1 : 
  - hello-service

TP2 :
 - produit-service
 - client-service