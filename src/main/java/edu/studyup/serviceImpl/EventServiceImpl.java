package edu.studyup.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.studyup.entity.Event;
import edu.studyup.entity.Student;
import edu.studyup.service.EventService;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

public class EventServiceImpl implements EventService {

	@Override
	public Event updateEvent(Event event) {
		DataStorage.eventData.put(event.getEventID(), event);
		event = DataStorage.eventData.get(event.getEventID());
		return event;
	}

	@Override
	public List<Event> getActiveEvents() {
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> activeEvents = new ArrayList<>();
		
		// Checks if an event date is before today, if no, then add to the active event list.
		for (int i=0;i<eventData.size();i++) {
			Event ithEvent= eventData.get(i);
			System.out.println(ithEvent.getDate());
			if(!ithEvent.getDate().before(new Date())) {
				activeEvents.add(ithEvent);
			}
		}
		return activeEvents;
	}

	@Override
	public List<Event> getPastEvents() {
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> pastEvents = new ArrayList<>();
		
		// Checks if an event date is before today, if no, then add to the active event list.
		for (int i=0;i<eventData.size();i++) {
			Event ithEvent= eventData.get(i);
			if(ithEvent.getDate().before(new Date())) {
				pastEvents.add(ithEvent);
			}
		}
		return pastEvents;
	}

	@Override
	public boolean addStudentToEvent(Student student, int eventID) throws Exception {
		Event event = DataStorage.eventData.get(eventID);
		if(event == null) {
			throw new StudyUpException("No event found.");
		}
		List<Student> presentStudents = event.getStudents();
		//Todo Check
		presentStudents.add(student);
		event.setStudents(presentStudents);
		DataStorage.eventData.put(eventID, event);
		return false;
	}

	@Override
	public boolean deleteEvent(int eventID) {
		DataStorage.eventData.remove(eventID);
		//Todo error
		return false;
	}

}
