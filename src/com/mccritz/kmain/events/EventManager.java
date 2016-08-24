package com.mccritz.kmain.events;

import com.mccritz.kmain.events.end.EndEvent;
import com.mccritz.kmain.kMain;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

public class EventManager {

    public kMain main = kMain.getInstance();
    public Set<Event> eventSet = new HashSet<>();
    public MongoCollection<Document> eventCollection = main.getMongoDatabase().getCollection("events");

    public EventManager() {
        eventSet.add(new EndEvent());

        System.out.println("Events: " + eventSet.toString());
    }

    public EndEvent getEndEvent() {
        for (Event event : eventSet) {
            if (event instanceof EndEvent) {
                return (EndEvent) event;
            }
        }

        return null;
    }

    public Set<Event> getEventSet() {
        return eventSet;
    }
}
