package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Collection<?> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(),
                "В понедельник должна быть ровно 1 тренировка");

        Collection<?> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(),
                "Во вторник расписание должно быть пустым");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Collection<List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size(), "В понедельник должно быть 1 занятие");

        Collection<List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
       assertEquals(2, thursdaySessions.size(), "В четверг должно быть 2 занятия");

        List<TrainingSession> thursdayList = new ArrayList<>();

        for (List<TrainingSession> hourList : thursdaySessions) {
            thursdayList.addAll(hourList);
        }


        assertEquals(thursdayChildTrainingSession, thursdayList.get(0),
                "Первой должна идти дневная тренировка в 13:00");

        assertEquals(thursdayAdultTrainingSession, thursdayList.get(1),
                "Второй должна идти вечерняя тренировка в 20:00");

        Collection<List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty(), "Во вторник должно быть пусто");

    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> monday13Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, monday13Sessions.size(),
                "В понедельник в 13:00 должна быть ровно 1 тренировка");
        assertEquals(singleTrainingSession, monday13Sessions.get(0),
                "Вернувшееся занятие должно совпадать с добавленным");

        List<TrainingSession> monday14Sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(monday14Sessions.isEmpty(),
                "В понедельник в 14:00 расписание должно быть пустым");

    }
    @Test
    void testGetCoachActivityWhenOneCoachIsMoreActive() {
        Timetable timetable = new Timetable();

        Group firstGroup = new Group("Акробатика для детей", Age.CHILD, 60);
        Group secondGroup = new Group("Рукопашный бой", Age.ADULT, 60);
        Group thirdGroup = new Group("Миксфайт", Age.ADULT, 60);
        Coach firstCoach = new Coach("Рамов", "Андрей", "Николаевич");
        Coach secondCoach = new Coach("Кубарев", "Алексей", "Константинович");
        TrainingSession firstSingleTrainingSession = new TrainingSession(secondGroup, firstCoach,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0));
        TrainingSession secondSingleTrainingSession = new TrainingSession(firstGroup, firstCoach,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession thirdSingleTrainingSession = new TrainingSession(thirdGroup, secondCoach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(firstSingleTrainingSession);
        timetable.addNewTrainingSession(secondSingleTrainingSession);
        timetable.addNewTrainingSession(thirdSingleTrainingSession);

        List<Coach> activeCoaches = timetable.getCoachActivity();

        assertEquals(2, activeCoaches.size(), "В рейтинге должно быть 2 тренера");
        assertEquals(firstCoach, activeCoaches.get(0), "Первым должен идти Рамов (2 занятия)");
        assertEquals(secondCoach, activeCoaches.get(1), "Вторым должен идти Кубарев (1 занятие)");
    }
    @Test
    void testGetCoachActivityWhenBothCoachesAreEqual() {
        Timetable timetable = new Timetable();

        Group firstGroup = new Group("Акробатика для детей", Age.CHILD, 60);
        Group secondGroup = new Group("Рукопашный бой", Age.ADULT, 60);
        Group thirdGroup = new Group("Миксфайт", Age.ADULT, 60);
        Group fourthGroup = new Group("Бокс для малышей", Age.CHILD, 60);
        Coach firstCoach = new Coach("Рамов", "Андрей", "Николаевич");
        Coach secondCoach = new Coach("Кубарев", "Алексей", "Константинович");
        TrainingSession firstSingleTrainingSession = new TrainingSession(secondGroup, firstCoach,
                DayOfWeek.TUESDAY, new TimeOfDay(14, 0));
        TrainingSession secondSingleTrainingSession = new TrainingSession(firstGroup, firstCoach,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));
        TrainingSession thirdSingleTrainingSession = new TrainingSession(thirdGroup, secondCoach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0));
        TrainingSession fourthSingleTrainingSession = new TrainingSession(fourthGroup, secondCoach,
                DayOfWeek.SATURDAY, new TimeOfDay(9, 0));


        timetable.addNewTrainingSession(firstSingleTrainingSession);
        timetable.addNewTrainingSession(secondSingleTrainingSession);
        timetable.addNewTrainingSession(thirdSingleTrainingSession);
        timetable.addNewTrainingSession(fourthSingleTrainingSession);

        List<Coach> activeCoaches = timetable.getCoachActivity();


        assertEquals(2, activeCoaches.size(),
                "При равной активности в списке должно быть ровно 2 тренера");
        assertTrue(activeCoaches.contains(firstCoach),
                "Список должен содержать тренера Рамова");
        assertTrue(activeCoaches.contains(secondCoach),
                "Список должен содержать тренера Кубарева");
    }
    @Test
    void testGetCoachActivityWhenTimetableIsEmpty() {
        Timetable timetable = new Timetable();
        List<Coach> activeCoaches = timetable.getCoachActivity();

        Assertions.assertTrue(activeCoaches.isEmpty(),
                "Для пустого расписания список активности должен быть пустым");
    }


}
