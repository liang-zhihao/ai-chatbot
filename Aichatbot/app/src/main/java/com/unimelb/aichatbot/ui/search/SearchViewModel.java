package com.unimelb.aichatbot.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    class Friend {
        String AvatarUrl;
        String Name;

        public Friend(String avatarUrl, String name) {
            AvatarUrl = avatarUrl;
            Name = name;
        }

        public String getAvatarUrl() {
            return AvatarUrl;
        }

        public String getName() {
            return Name;
        }
    }

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Friend>> mFriends;


    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Friends");
        mFriends = new MutableLiveData<>();

        List<Friend> friends = new LinkedList<>();
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9493/1b8b/405a3aec1b73b0d0d03ab04af51540b1?Expires=1695600000&Signature=VHjMb~yZfycvcAd3bIoVoncGm87wmNu5CKVmvjti6y9jx3J4cYj-1pgf0Vn5r8Oa453wywtZ4wro7z048LRLoRvUjnHA6eP4qaoo-C4brtQZ4ibruG7RZNRSqyZpApOe91N-3t~IoHLoq2XvRkah7uVPLs6c5b3UooldTgeLoTVoMPjgfLxFgTImY5f4Z82XrLTs5XyjGE5e5-ENnqWY-IvM2BQJF1tHMaiFUMg5mdHsqMxrcmSMW6djz1Q5KKhL89uRlKpLwPisHTeKCFEpIGoRbLYcyag1gUcRJKzcwPEgu4F~Nf9pe-Rdty65W5~E5wPkT~j2ujhBKQgVxueHKA__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Beth Murphy"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/9261/dba4/707be4fc468fe699496620292830adad?Expires=1695600000&Signature=NFoM2wIG4tu~zOzeMrRLLMjsyl1YIlkZcqykvdCiDrimnhxI8MazU0VHcLAXw8Ne4-GBFA~UAuoUz~3NVyAWcVvTuCp6j7YGffY6P-YDKy1oxFxERw69TaP-66DC7UTyoCsOZhDTiecildcSyd4F4fTP508AjpTTU0s5lNIOI98-qMKtPkZTxysRD9nTgRuR55cp-k-5wyjl97m4~jMStX3KEzRd46ehy-vOcMbTQsUyFazW7M7Tmz6EJrSzW8VrPGolADk8p6idXE8ySTILtX0tNRWWtFXOKBWJW5W-mP7rJoEhFuP1tkndthx7qh3yEt1VWsWeXRcSOFlvLgWaiw__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Bonelwa"));
        friends.add(new Friend("https://s3-alpha-sig.figma.com/img/75d2/4e87/1d3808434e5dbd8fee65aacbda0a1170?Expires=1695600000&Signature=R2vrlOuUoL2F~-6UiQGJ~YYNsepwzwau3AJ1Hpi36EKFKeJAV9rQmAQhcWk~GPCf03tZGWiES2zft~8KtG1NmluqJMsNz69mdgbrz4qCjqZs7EVHAunSSeJgOFwghccFJcxckx1YEO-cZT96og8~AHmEoV2YfG46w8lcqHlu41kH1CXqzg4Psk-jIstERP68KpIFuqNuEo~ilwyqc29W2RpscMT9wJ55Zj2cxqy5lVlfgOY0hHHzGLEZ91qMoFvBD1o9jO-alsrdZKXnJgrDHfIR9Xn8MXJkYvsPYqMoKU1GsE4GOJZpLSLLERClc5Q76omCgIl-CZBek2DkphLTbg__&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4","Leonardo Oliveira"));

        mFriends.postValue(friends);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Friend>> getFriends() {
        return  mFriends;
    }
}
