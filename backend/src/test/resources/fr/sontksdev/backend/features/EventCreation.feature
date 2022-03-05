#language:fr
Feature: Création d'événement

  En tant qu'organisateur, je veux créer des événements

Scénario: Laura va créer un événement avec les informations qu'on lui a donné, on a alors l'événement correspondant qui est créé
Étant donné un événement "Nuit de l'info" avec maximum "100" personnes commencant le "23/03/2022" et se terminant le "23/03/2022"
Quand les informations sont passées au controller
Alors l'événement "Nuit de l'info" est créé
