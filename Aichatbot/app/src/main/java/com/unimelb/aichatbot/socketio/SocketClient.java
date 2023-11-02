package com.unimelb.aichatbot.socketio;

import com.google.gson.Gson;
import com.unimelb.aichatbot.BuildConfig;
import com.unimelb.aichatbot.socketio.dto.InitializeConnectionData;
import com.unimelb.aichatbot.socketio.dto.MessageEvents;
import com.unimelb.aichatbot.socketio.dto.RoomData;
import com.unimelb.aichatbot.socketio.dto.RoomMessageData;

import android.util.Log;


import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketClient {
    private final Gson gson = new Gson();
    private static final String BASE_URL = BuildConfig.SOCKET_URI;

    public static final String TAG = SocketClient.class.getSimpleName();
    private static SocketClient mInstance;
    private final Emitter.Listener onConnect = args -> Log.d(TAG, "Socket Connected!");
    private final Emitter.Listener onConnectError = args -> Log.d(TAG, "onConnectError");
    private final Emitter.Listener onDisconnect = args -> Log.d(TAG, "onDisconnect");
    private Socket socket;

    private SocketClient() {
        initializeSocket();
    }

    public static SocketClient getInstance() {
        if (mInstance == null) {
            synchronized (SocketClient.class) {
                if (mInstance == null) {
                    mInstance = new SocketClient();
                }
            }
        }
        return mInstance;
    }

    private void initializeSocket() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.reconnectionDelay = 2000;
        options.reconnectionDelayMax = 5000;

        try {
            socket = IO.socket(BASE_URL + ":8001");
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            if (!socket.connected()) {
                socket.connect();
                registerListener();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private void registerListener() {

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Connected!");
            // Initialize connection
            InitializeConnectionData initData = new InitializeConnectionData();
            // TODO access username from shared preferences
            initData.setUserId("JohnDoe");
            String event = MessageEvents.INITIALIZE_CONNECTION.getStr();
            BaseEvent<InitializeConnectionData> initEvent = new BaseEvent<>();
            initEvent.setEvent(MessageEvents.INITIALIZE_CONNECTION.getStr());
            initEvent.setData(initData);

            emit(event, initEvent);
        });
        //  receive message listener
        // socket.on(MessageEvents.MESSAGE.getStr(), args -> {
        //     String json = (String) args[0];
        //     BaseEvent<RoomMessageData> event = gson.fromJson(json, BaseEvent.class);
        //     System.out.println("Message received: " + event.getData().getMessage());
        // });


    }

    public void joinRoom(String roomName) {
        RoomData roomData = new RoomData();
        roomData.setRoom_id(roomName);
        String event = MessageEvents.JOIN_ROOM.getStr();
        BaseEvent<RoomData> joinEvent = new BaseEvent<>();
        joinEvent.setEvent(event);
        joinEvent.setData(roomData);


        emit(event, joinEvent, (Ack) args -> {
            System.out.println("Join room ack: " + args[0]);
        });
    }

    public void sendMessage(String roomName, String message) {
        RoomMessageData messageData = new RoomMessageData();
        messageData.setRoomId(roomName);
        messageData.setMessage(message);
        String event = MessageEvents.MESSAGE.getStr();
        BaseEvent<RoomMessageData> messageEvent = new BaseEvent<>();
        messageEvent.setEvent(event);
        messageEvent.setData(messageData);
        emit(event, messageEvent);
    }

    public void leaveRoom(String roomName) {
        RoomData roomData = new RoomData();
        roomData.setRoom_id(roomName);
        String event = MessageEvents.LEAVE_ROOM.getStr();
        BaseEvent<RoomData> leaveEvent = new BaseEvent<>();
        leaveEvent.setEvent(event);
        leaveEvent.setData(roomData);
        emit(event, leaveEvent);
    }

    private void emit(String event, BaseEvent<?> data, Ack ack) {
        socket.emit(event, gson.toJson(data));
    }

    private void emit(String event, BaseEvent<?> data) {
        socket.emit(event, gson.toJson(data));
    }

    public void on(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public void stop() {
        socket.disconnect();
    }

}

