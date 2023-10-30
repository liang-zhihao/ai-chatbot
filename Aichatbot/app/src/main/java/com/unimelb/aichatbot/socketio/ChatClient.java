package com.unimelb.aichatbot.socketio;

import com.google.gson.Gson;
import com.unimelb.aichatbot.BuildConfig;
import com.unimelb.aichatbot.socketio.dto.InitializeConnectionData;
import com.unimelb.aichatbot.socketio.dto.PrivateMessageData;
import com.unimelb.aichatbot.socketio.dto.RoomData;
import com.unimelb.aichatbot.socketio.dto.RoomMessageData;

import android.util.Log;


import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class ChatClient {
    private final Gson gson = new Gson();
    private static final String BASE_URL = BuildConfig.SOCKET_URI;

    public static final String TAG = ChatClient.class.getSimpleName();
    private static ChatClient mInstance;
    private final Emitter.Listener onConnect = args -> Log.d(TAG, "Socket Connected!");
    private final Emitter.Listener onConnectError = args -> Log.d(TAG, "onConnectError");
    private final Emitter.Listener onDisconnect = args -> Log.d(TAG, "onDisconnect");
    private Socket socket;

    private ChatClient() {
        initializeSocket();
    }

    public static ChatClient getInstance() {
        if (mInstance == null) {
            synchronized (ChatClient.class) {
                if (mInstance == null) {
                    mInstance = new ChatClient();
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
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    private void registerMessageListener() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Connected!");
            // Initialize connection
            InitializeConnectionData initData = new InitializeConnectionData();
            // TODO access username from shared preferences
            initData.setUserId("JohnDoe");
            BaseEvent<InitializeConnectionData> initEvent = new BaseEvent<>();
            initEvent.setEvent("initialize_connection");
            initEvent.setData(initData);

            socket.emit("initialize_connection", gson.toJson(initEvent));
        });

        socket.on("receive_message", args -> {
            String json = (String) args[0];
            BaseEvent<RoomMessageData> event = gson.fromJson(json, BaseEvent.class);
            System.out.println("Message received: " + event.getData().getMessage());
        });

        socket.on("receive_private_message", args -> {
            String json = (String) args[0];
            BaseEvent<PrivateMessageData> event = gson.fromJson(json, BaseEvent.class);
            System.out.println("Private message received from " + event.getData().getFromUser());
        });
    }

    public void joinRoom(String roomName) {
        RoomData roomData = new RoomData();
        roomData.setRoom_id(roomName);

        BaseEvent<RoomData> joinEvent = new BaseEvent<>();
        joinEvent.setEvent("join_room");
        joinEvent.setData(roomData);

        socket.emit("join_room", gson.toJson(joinEvent), (Ack) args -> {
            System.out.println("Join room ack: " + args[0]);
        });
    }

    public void sendMessage(String roomName, String message) {
        RoomMessageData messageData = new RoomMessageData();
        messageData.setRoomId(roomName);
        messageData.setMessage(message);

        BaseEvent<RoomMessageData> messageEvent = new BaseEvent<>();
        messageEvent.setEvent("send_message");
        messageEvent.setData(messageData);

        socket.emit("send_message", gson.toJson(messageEvent));
    }

    public void leaveRoom(String roomName) {
        RoomData roomData = new RoomData();
        roomData.setRoom_id(roomName);

        BaseEvent<RoomData> leaveEvent = new BaseEvent<>();
        leaveEvent.setEvent("leave_room");
        leaveEvent.setData(roomData);

        socket.emit("leave_room", gson.toJson(leaveEvent));
    }

    public void sendPrivateMessage(String toUser, String message) {
        PrivateMessageData privateMessageData = new PrivateMessageData();
        privateMessageData.setToUser(toUser);
        privateMessageData.setMessage(message);

        BaseEvent<PrivateMessageData> privateMessageEvent = new BaseEvent<>();
        privateMessageEvent.setEvent("send_private_message");
        privateMessageEvent.setData(privateMessageData);

        socket.emit("send_private_message", gson.toJson(privateMessageEvent));
    }

    public void start() {
        initializeSocket();
    }

    public void stop() {
        socket.disconnect();
    }
}

