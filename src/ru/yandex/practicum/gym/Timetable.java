package ru.yandex.practicum.gym;


import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>(10);

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<TimeOfDay, List<TrainingSession>>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayTree = timetable.get(day);

        if (!dayTree.containsKey(time)) {
            dayTree.put(time, new ArrayList<TrainingSession>());
        }
        List<TrainingSession> currentSessions = dayTree.get(time);
        Coach newCoach = trainingSession.getCoach();

        for (TrainingSession existingSession : currentSessions) {
            if (existingSession.getCoach().equals(newCoach)) {
                System.out.println("Ошибка: Тренер " + newCoach + " уже занят в это время!");
                return;
            }
        }

        currentSessions.add(trainingSession);
        System.out.println("Тренировка успешно добавлена");
    }

    public Collection<List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (!timetable.containsKey(dayOfWeek)) {
            return Collections.emptyList();
        }

        return timetable.get(dayOfWeek).values();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (!timetable.containsKey(dayOfWeek)) {
            return new ArrayList<>();
        }
        TreeMap<TimeOfDay, List<TrainingSession>> dayTree = timetable.get(dayOfWeek);
        List<TrainingSession> sessions = dayTree.get(timeOfDay);
        if (sessions == null) {
            return new ArrayList<>();
        }
        return sessions;
    }

    public List<Coach> getCoachActivity() {

        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayTree : timetable.values()) {

            for (List<TrainingSession> hourList : dayTree.values()) {

                for (TrainingSession session : hourList) {
                    Coach coach = session.getCoach();

                    if (coachCounts.containsKey(coach)) {
                        int currentCount = coachCounts.get(coach);
                        coachCounts.put(coach, currentCount + 1);
                    } else {
                        coachCounts.put(coach, 1);
                    }
                }
            }
        }

        List<Coach> sortedCoaches = new ArrayList<>(coachCounts.keySet());

        sortedCoaches.sort(new Comparator<Coach>() {
            @Override
            public int compare(Coach c1, Coach c2) {
                int count1 = coachCounts.get(c1);
                int count2 = coachCounts.get(c2);

                return count2 - count1;
            }
        });

        return sortedCoaches;
    }


}

