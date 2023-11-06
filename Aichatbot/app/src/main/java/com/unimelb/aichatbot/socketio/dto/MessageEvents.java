package com.unimelb.aichatbot.socketio.dto;

public enum MessageEvents {

    INITIALIZE_CONNECTION("initialize_connection"),
    MESSAGE_TO_SERVER("message_to_server"),
    JOIN_ROOM("join_room"),
    LEAVE_ROOM("leave_room");

    private final String eventName;

    MessageEvents(String eventName) {
        this.eventName = eventName;
    }

    public String getStr() {
        return this.eventName;
    }

    // Utility method to get enum by string value
    public static MessageEvents fromString(String eventName) {
        for (MessageEvents event : MessageEvents.values()) {
            if (event.getStr().equalsIgnoreCase(eventName)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Event not found: " + eventName);
    }
}

