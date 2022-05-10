package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.components.interfaces.ActivityFinder;
import fr.stonksdev.backend.components.interfaces.ActivityModifier;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.components.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ActivityManager implements ActivityModifier, ActivityFinder {

    @Autowired
    private ActivityRepository activityRepo;

    @Override
    @Transactional
    public Activity createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, StonksEvent stonksEvent) throws AlreadyExistingActivityException {
        Activity newActivity = new Activity(beginning, duration, name, maxPeopleAmount, stonksEvent);
        if (findByName(name).isPresent()) {
            throw new AlreadyExistingActivityException(name);
        }
        Activity activity = activityRepo.save(newActivity);
        stonksEvent.addActivity(activity);
        return activity;
    }

    @Override
    @Transactional
    public Activity createActivity(LocalDateTime beginning, Duration duration, String name, StonksEvent stonksEvent) throws AlreadyExistingActivityException {
        Activity newActivity = new Activity(beginning, duration, name, stonksEvent);
        if (findByName(name).isPresent()) {
            throw new AlreadyExistingActivityException(name);
        }
        Activity activity = activityRepo.save(newActivity);
        stonksEvent.addActivity(activity);
        return activity;
    }

    @Override
    @Transactional
    public Activity updateActivity(Long activityId, int maxPeopleAmount, LocalDateTime startDate, Duration duration) throws ActivityNotFoundException {
        Optional<Activity> activityOpt = findById(activityId);
        if (activityOpt.isEmpty()) {
            throw new ActivityNotFoundException(activityId.toString());
        }
        Activity activity = activityOpt.get();
        activity.setMaxPeopleAmount(maxPeopleAmount);
        activity.setBeginning(startDate);
        activity.setDuration(duration);
        return activityRepo.save(activity);
    }

    @Override
    @Transactional
    public void deleteActivity(Activity activity) {
        activity.getEvent().getActivities().remove(activity);
        activityRepo.delete(activity);
    }

    @Override
    public List<Activity> getAllActivities() {
        return activityRepo.findAll();
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return activityRepo.findActivityByName(name);
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepo.findById(id);
    }
}
