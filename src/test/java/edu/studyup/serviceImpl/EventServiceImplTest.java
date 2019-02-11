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
	
	@Test // NEW TEST
	void testUpdateEvent_testName_badCase() throws StudyUpException {
		int eventID = 1;
				
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 1 This name is too long");
		  });	
	}
	
	@Test // This is a bug since event names can be length 20, but an exception is thrown instead.
	void testUpdateEvent_testName_bugCase() throws StudyUpException {
		int eventID = 1;
				
		eventServiceImpl.updateEventName(eventID, "This is Event 123456");
		assertEquals(DataStorage.eventData.get(eventID).getName(), "This is Event 123456");
	}
	
	@Test // NEW TEST
	void testaddStudentToEvent_nullEventcase() {
		int eventID = 3;
		Student student = new Student();
		student.setFirstName("Lone");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student, eventID);
		  });
	}
	
	@Test // NEW TEST
	void testaddStudentToEvent_AddingOneStudent() throws StudyUpException{
		
		Student student = new Student();
		student.setFirstName("Jack");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		Event event = new Event();
		event.setEventID(100);
		event.setDate(new Date());
		event.setName("Event 100");
		Location location = new Location(-1223, 37);
		event.setLocation(location);
		
		DataStorage.eventData.put(event.getEventID(), event);
		eventServiceImpl.addStudentToEvent(student, event.getEventID());
		assertEquals(student, DataStorage.eventData.get(event.getEventID()).getStudents().get(0));
	}
	
	@Test // NEW TEST
	void testaddStudentToEvent_StudentArrayisNotNull() throws StudyUpException{
		
		Student student = new Student();
		student.setFirstName("Jack");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		Student newstudent = new Student();
		newstudent.setFirstName("Jill");
		newstudent.setLastName("Doe");
		newstudent.setEmail("JillDoe@email.com");
		newstudent.setId(222);
		
		Event event = new Event();
		event.setEventID(101);
		event.setDate(new Date());
		event.setName("Event 101");
		Location location = new Location(-1223, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
		eventServiceImpl.addStudentToEvent(newstudent, event.getEventID());
		assertEquals(newstudent, DataStorage.eventData.get(event.getEventID()).getStudents().get(1));
	}
	
	@Test // This is a bug since only two students can be in an event at a time, but I added three students into the event.
	void testaddStudentToEvent_MaxTwoStudentsinEvent() throws StudyUpException{
		
		Student student = new Student();
		student.setFirstName("Jack");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		Student student2 = new Student();
		student2.setFirstName("Jill");
		student2.setLastName("Doe");
		student2.setEmail("JillDoe@email.com");
		student2.setId(222);
		
		Student student3 = new Student();
		student3.setFirstName("Johnny");
		student3.setLastName("Doe");
		student3.setEmail("JohnnyDoe@email.com");
		student3.setId(333);
		
		Event event = new Event();
		event.setEventID(102);
		event.setDate(new Date());
		event.setName("Event 102");
		Location location = new Location(-1, 37);
		event.setLocation(location);
		
		DataStorage.eventData.put(event.getEventID(), event);
		
		eventServiceImpl.addStudentToEvent(student, event.getEventID());
		eventServiceImpl.addStudentToEvent(student2, event.getEventID());
		eventServiceImpl.addStudentToEvent(student3, event.getEventID());
		
		
		assertEquals(event.getStudents().size(), 2);	
	}
}
