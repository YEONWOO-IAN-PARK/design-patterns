package me.whiteship.designpatterns._03_behavioral_patterns._19_observer._01_before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {

    // 메시지를 key, value 쌍으로 저장하는 메세지 맵 저장소
    private Map<String, List<String>> messages;

    // 클래스가 생성되면 메세지 맵 저장소를 초기화시킨다.
    public ChatServer() {
        this.messages = new HashMap<>();
    }

    // 메시지 저장소에서 키로 찾은 메시지 리스트에 메세지를 저장하는 메서드
    public void add(String subject, String message) {

        if (messages.containsKey(subject)) {
            messages.get(subject).add(message);
        } else {
            List<String> messageList = new ArrayList<>();   
            messageList.add(message);
            messages.put(subject, messageList);
        }
    }

    // 메시지 저장소에서 특정 메시지 리스트를 찾는 기능
    public List<String> getMessage(String subject) {
        return messages.get(subject);
    }
}
