package fr.stonksdev.backend.components.exceptions;

public class ActivityNotFoundException extends Exception{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ActivityNotFoundException() {
        }

        public ActivityNotFoundException(String activityName) {
            this.name = activityName;
        }
}
