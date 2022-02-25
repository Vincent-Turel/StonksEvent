package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Equipment;

import java.util.List;

public interface CheckMaterial {
    List<Equipment> check(List<Equipment> equipmentToCheck);
}
