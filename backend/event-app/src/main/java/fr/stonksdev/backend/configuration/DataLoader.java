package fr.stonksdev.backend.configuration;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.components.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

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

        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        Example<Room> example314 = Example.of(room314, modelMatcher);
        Example<Room> example106 = Example.of(room106, modelMatcher);
        Example<Room> exampleForum = Example.of(amphiForum, modelMatcher);
        Example<Room> example228 = Example.of(amphi228, modelMatcher);

        if (!roomRepo.exists(example314))
            roomRepo.saveAndFlush(room314);

        if (!roomRepo.exists(example106))
            roomRepo.saveAndFlush(room106);

        if (!roomRepo.exists(exampleForum))
            roomRepo.saveAndFlush(amphiForum);

        if (!roomRepo.exists(example228))
            roomRepo.saveAndFlush(amphi228);
    }
}