package com.unimelb.aichatbot.socketio;

public class BaseEvent<T> {
    private String event;
    private T data;

    public BaseEvent() {
    }

    public BaseEvent(String event, T data) {
        this.event = event;
        this.data = data;
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // Getters and setters
}
// Base class for all message types


// Data for sending or receiving messages in a room


// Data for joining or leaving a room


// Data for sending or receiving private messages


// Data for initializing a connection


// BaseEvent class


// { "event": "receive_message", "data": { "room_id": "room_name", "message": "message_content" } }
//
//         { "event": "send_message", "data": { "room_id": "room_name", "message": "message_content" } }
//
//         { "event": "join_room", "data": { "room_id": "room_name" } }
//
//         { "event": "leave_room", "data": { "room_id": "room_name" } }
//
//         { "event": "send_private_message", "data": { "to_user": "user_name", "message": "message_content" } }
//
//         { "event": "receive_private_message", "data": { "from_user": "user_name", "message": "message_content" } }
//
//         {
//         "event": "initialize_connection",
//         "data": {
//         "user_id": "user_name",
//         }
//         }
