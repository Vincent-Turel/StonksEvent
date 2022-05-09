# ISA-DevOps - livrable.md

## Compilation locale

### Programmes requis

Les programmes suivants doivent être installés :
- `git`,
- `docker`,
- `make`,
- `ssh`.

### Construction

Exécuter les commandes suivantes dans un shell :

```shell
git clone git@github.com:pns-isa-devops/isa-devops-21-22-team-h-2022.git
cd isa-devops-21-22-team-h-2022
make images
```

#### Pourquoi utiliser `make` ?

On utilisait au départ des petits scripts bash, mais la gestion d'erreur inexistante de ce langage nous a forcé à nous
tourner vers une solution telle que `make` il a également été considéré d'utiliser [`just`] car il est plus facile à
prendre en main et propose les mêmes fonctionnalités. Cette solution n'a pas été retenue car [`just`] n'est pas
distribué en binaire sur Ubuntu (pour la VM Proxmox) ni sur Debian (pour le container Jenkins) et qu'il serait *
légèrement overkill* de le compiler à partir des sources juste pour le projet.

[`just`]: https://github.com/casey/just

## Répartition des tâches ISA/DevOps

En ISA comme en DevOps, nous estimons que les tâches ont été réparties équitablement entre les membres de l'équipe. Nous
donnons donc les scores de 100 - 100 - 100 - 100 pour chaque membre et pour chaque discipline.

## Pipelines

Lorsqu'un commit est push sur `master`, les actions suivantes sont déclenchées :

- on build les images et on les déploie sur le docker hub,

Lorsqu'un commit est push sur `develop`, les actions suivantes sont déclenchées :

- on build les images et on les démarre localement depuis jenkins,
- on build chaque composant et on le push sur artifactory,

Lorsqu'un commit est push sur une autre branche, les actions suivantes sont déclenchées :

- on run les tests.