Feature: Event Creation

  This feature support the way an organizer can create a new event and its activities

  Background:
    Given a name "StonksEvent" for an event

  Scenario:
    When "Laura" create an event named "StonksEvent" with 150 people between the 1 - 1 - 2022 , 8 h 0, and 15 - 1 - 2022 , 8 h 0
    Then there is 1 event
    And its name is "StonksEvent"
    And the number of people is 150
    And the start date is 1 - 1 - 2022 , 8 h 0
    And the end date is 15 - 1 - 2022 , 8 h 0

  Scenario:
    When "Laura" create an event named "StonksEvent" with 150 people between the 1 - 1 - 2022 , 8 h 0, and 15 - 1 - 2022 , 8 h 0
    Then she change the amount of people to 200

  Scenario:
    When "Pierre" create an event named "MagicCode" with 300 people between the 1 - 1 - 2022 , 8 h 0, and 15 - 1 - 2022 , 8 h 0
    And "Pierre" create an activity named "CubeX" with 100 people, the 1 - 1 - 2022 , 9 h 0 , for 150 minutes
    Then there is only 1 activity on the event named "MagicCode"