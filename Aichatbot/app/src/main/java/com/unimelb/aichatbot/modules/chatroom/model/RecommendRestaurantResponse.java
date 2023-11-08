package com.unimelb.aichatbot.modules.chatroom.model;

public class RecommendRestaurantResponse {
//      {
//         "reply": "Greetings, my dear users. As Gordon Ramsay, I would like to give you some advice on which restaurant to visit based on the provided information. After analyzing the reviews, it seems that the best option would be DOC Pizza & Mozzarella Bar - Carlton. This restaurant has a high rating of 4.3, and many customers have praised their delicious pizzas and great service. Furthermore, the restaurant has a good atmosphere, and it is recommended to book in advance due to its popularity. While the other restaurants have positive reviews, they also have some mixed feedback, indicating that they might not be the best choice. Therefore, I highly recommend visiting DOC Pizza & Mozzarella Bar - Carlton for a delightful dining experience.",
//         "time": "2023-11-02 13:18:24"
//     },

    private String reply;
    private String time;

    public RecommendRestaurantResponse(String reply, String time) {
        this.reply = reply;
        this.time = time;
    }

    public String getReply() {
        return reply;
    }

    public String getTime() {
        return time;
    }
}
