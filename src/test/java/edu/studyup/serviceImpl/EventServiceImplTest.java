package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void getActiveEvents_allEventsAfterCurrentDate() {
		Event pastEvent = new Event();
		pastEvent.setEventID(2);
		pastEvent.setDate(new Date(0));
		
		DataStorage.eventData.put(pastEvent.getEventID(), pastEvent);
		
		for(Event e: eventServiceImpl.getActiveEvents()) {
			assertTrue(e.getDate().after(new Date()));
		}
	}
	
	@Test
	void getActiveEvents_ensureNewEventInActiveEvents() {
		Event futureEvent = new Event();
		futureEvent.setEventID(2);
		futureEvent.setDate(new Date((long) (System.currentTimeMillis() * 1.5)));
		
		DataStorage.eventData.put(futureEvent.getEventID(), futureEvent);
		
		assertTrue(eventServiceImpl.getActiveEvents().contains(futureEvent));
	}
	
	@Test
	void getPastEvents_allEventsBeforeCurrentDate() {
		Event futureEvent = new Event();
		futureEvent.setEventID(2);
		futureEvent.setDate(new Date((long) (System.currentTimeMillis() * 1.5)));
		
		DataStorage.eventData.put(futureEvent.getEventID(), futureEvent);
		
		for(Event e: eventServiceImpl.getPastEvents()) {
			assertTrue(e.getDate().before(new Date()));
		}
	}
	
	@Test
	void getPastEvents_ensurePastEventInGetPastEvents() {
		Event pastEvent = new Event();
		pastEvent.setEventID(2);
		pastEvent.setDate(new Date(0));
		
		DataStorage.eventData.put(pastEvent.getEventID(), pastEvent);
		
		assertTrue(eventServiceImpl.getPastEvents().contains(pastEvent));
	}
	
	@Test
	void deleteEvent_makeSureEventNotInDataStorage() {
		eventServiceImpl.deleteEvent(1);
		
		assertFalse(DataStorage.eventData.containsKey(1));
	}
	
	@Test
	void deleteEvent_returnsDeletedEvent() {
		Event event1 = DataStorage.eventData.get(1);
		assertTrue(eventServiceImpl.deleteEvent(1) == event1);
	}
}
