package com.mccritz.kmain.events;

import com.mccritz.kmain.kMain;
import org.bson.Document;

public class Event {

    private EventType type;
    private kMain main = kMain.getInstance();
    private EventManager eventManager = main.getEventManager();

    public Event(EventType type) {
        this.type = type;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public kMain getMain() {
        return main;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public Document toDocument() {
        return new Document("type", type.toString());
    }
}
