package com.unimelb.aichatbot.socketio;

import com.google.gson.Gson;
import com.unimelb.aichatbot.BuildConfig;
import com.unimelb.aichatbot.socketio.dto.InitializeConnectionData;
import com.unimelb.aichatbot.socketio.dto.MessageEvents;
import com.unimelb.aichatbot.socketio.dto.JoinRoomData;
import com.unimelb.aichatbot.socketio.dto.MessageToServerData;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketClient {
    private final Gson gson = new Gson();


    public static final String TAG = SocketClient.class.getSimpleName();
    private static SocketClient mInstance;
    private final Emitter.Listener onConnect = args -> Log.i(TAG, "Socket Connected!");
    private final Emitter.Listener onConnectError = args -> Log.i(TAG, "onConnectError");
    private final Emitter.Listener onDisconnect = args -> Log.i(TAG, "onDisconnect");
    private Socket socket;

    private SocketClient() {
        initializeSocket();
    }

    public static SocketClient getInstance() {

        synchronized (SocketClient.class) {
            if (mInstance == null) {
                mInstance = new SocketClient();
            }
        }

        return mInstance;
    }

    public static void startConnection() {
        getInstance();
    }

    private void initializeSocket() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.reconnectionDelay = 2000;
        options.reconnectionDelayMax = 5000;

        try {
            socket = IO.socket(BuildConfig.SERVER_LOCAL_URL);
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

    public void emitInitializeConnection(String userId) {
        InitializeConnectionData initData = new InitializeConnectionData();

        initData.setUserId(userId);
        String event = MessageEvents.INITIALIZE_CONNECTION.getStr();
        BaseEvent<InitializeConnectionData> initEvent = new BaseEvent<>();
        initEvent.setEvent(MessageEvents.INITIALIZE_CONNECTION.getStr());
        initEvent.setData(initData);
        emit(initEvent, args -> {
            if (args.length > 0) {
                Log.i(TAG, "Init connection ack: " + args[0]);
            } else {
                Log.i(TAG, "Init connection ack: " + "No args");
            }
        });
    }


    public void joinRoom(String roomId) {
        JoinRoomData roomData = new JoinRoomData(roomId);
        String event = MessageEvents.JOIN_ROOM.getStr();
        BaseEvent<JoinRoomData> joinEvent = new BaseEvent<>();
        joinEvent.setEvent(event);
        joinEvent.setData(roomData);


        emit(joinEvent, args -> {
            System.out.println("Join room ack: " + args[0]);
        });
    }

    public void emitSendMessage(String roomId, String message) {
        MessageToServerData messageData = new MessageToServerData();
        messageData.setRoomId(roomId);
        messageData.setMessage(message);
        String event = MessageEvents.MESSAGE_TO_SERVER.getStr();
        BaseEvent<MessageToServerData> messageEvent = new BaseEvent<>();
        messageEvent.setEvent(event);
        messageEvent.setData(messageData);
        emit(messageEvent);
    }

    public void leaveRoom(String roomId) {
        JoinRoomData roomData = new JoinRoomData(roomId);
        String event = MessageEvents.LEAVE_ROOM.getStr();
        BaseEvent<JoinRoomData> leaveEvent = new BaseEvent<>();
        leaveEvent.setEvent(event);
        leaveEvent.setData(roomData);
        emit(leaveEvent);
    }

    private void emit(BaseEvent<?> data, Ack ack) {
        try {

            socket.emit(data.getEvent(), new JSONObject(gson.toJson(data)), ack);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void emit(BaseEvent<?> data) {
        try {
            socket.emit(data.getEvent(), new JSONObject(gson.toJson(data)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void on(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public void stop() {
        socket.disconnect();
    }

}

