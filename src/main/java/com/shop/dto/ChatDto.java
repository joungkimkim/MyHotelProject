package com.shop.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class ChatDto {

    // 메시지  타입 : 입장, 채팅
    public enum MessageType{
        ENTER, TALK
    }

    public String type; // 메시지 타입
    public String roomId; // 방 번호
    public String sender; // 채팅을 보낸 사람
    public String message; // 메시지
    public String time; // 채팅 발송 시간간

}
