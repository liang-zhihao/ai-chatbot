package com.unimelb.aichatbot.modules.chatHistory.placeholder;

import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserChatHistory;
import com.unimelb.aichatbot.modules.chatHistory.responsObject.UserRoles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();

    private static final int COUNT = 5;

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createPlaceholderItem(i));
//        }
//    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

//    private static PlaceholderItem createPlaceholderItem(UserChatHistory userChatHistory) {
//
//        //display the last chat
////        List<UserChatHistory.ChatMessage> list = userChatHistory.getData().getChatHistory();
////        return new PlaceholderItem("11", list.get(list.size() - 1).getContent(), "");

//    }

    private static PlaceholderItem createPlaceholderItem(String role) {

        //display the last chat
        return new PlaceholderItem(role, "", "",role);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String content;
        public final String details;
        public final String image;

        public PlaceholderItem(String id, String content, String details, String image) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.image = image;
        }

        @Override
        public String toString() {
            return content;
        }
    }



    public static void toggleItems() {
//        if (ITEMS.size() == 5) {
//            for (int i = 6; i <= COUNT; i++) {
//                addItem(createPlaceholderItem(i));
//            }
//        } else {
//            while (ITEMS.size() > 5) {
//                ITEMS.remove(ITEMS.size() - 1);
//            }
//        }
    }

//    public static void display(List<UserChatHistory> userChatHistoryList) {
//        ITEMS.clear();
//        for(UserChatHistory u:userChatHistoryList){
//            addItem(createPlaceholderItem(u));
//        }
//
//    }

    public static void display(UserRoles userRoles) {
        ITEMS.clear();
        for(String  u:userRoles.getData().getRoles()){
            addItem(createPlaceholderItem(u));
        }

    }

}