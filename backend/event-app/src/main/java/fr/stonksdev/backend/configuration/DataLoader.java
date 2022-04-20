package fr.stonksdev.backend.configuration;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.components.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private RoomRepository roomRepo;

    @Autowired
    public DataLoader(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public void run(ApplicationArguments args) {
        Room room314 = new Room("O+314", RoomKind.Classroom, 20);
        Room room106 = new Room("O+106", RoomKind.Classroom, 16);
        Room amphiForum = new Room("Amphi Forum", RoomKind.Amphitheatre, 200);
        Room amphi228 = new Room("O+228", RoomKind.Amphitheatre, 120);

        roomRepo.saveAllAndFlush(List.of(room314, room106, amphiForum, amphi228));
    }
}