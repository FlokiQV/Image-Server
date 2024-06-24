
# Projet client-serveur

Projet PDL réalisé par WARDEH Amir.

## Lancer le projet

*L'extension Vite de VS Code doit être installée pour lancer le frontend*

### Étapes

Se placer à la racine du projet

Dans un terminal

- cd backend

- mvn spring-boot:run

- ou Run /Start Debugging dans le VS Code

Dans un autre terminal

- cd frontend

- npm install

- npm run dev

## Tests du projet et package node_modules

Notre serveur a été testé sur Linux Ubuntu 22.04.2 LTS , Windows (WSL) ainsi que sur un système MacOS

Cependant le package node_module n'étant pas similaire pour les deux systèmes nous avons du l'installer chacun de notre côté avec la commande :

- supprimer le dossier node_modules
- faire la commande : **cd frontend; npm install**

Notre client web a été testé sur Google Chrome ainsi que Safari.


## Extensions


- Amélioration générale
  - Code commenté
  - Code inutile et synthétisé pour le fichier regroupant les filtres (ImagesAlgortihmes.java)
  - Ajouts de test au sein du code
- Amélioration frontend
  - Épuration graphique pour rendre le site plus ergonomique
  - Mise en place d'un menu de selection de chaque filtre
  - Interface utilisateur modifiée en retirant les entrées de texte mais en favorisant l'utilisation de selecteur et/ou curseur de défilement

# Image-Server
