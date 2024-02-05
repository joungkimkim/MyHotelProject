package com.shop.entity;

import com.shop.dto.ChatDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat")
@ToString
public class Chat {
    @Id
    @Column(name = "chat_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;
    private ChatDto.MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; // 채팅 발송 시간간

    public Chat createChat(ChatDto chatDto){
        Chat chat =new Chat();
        chat.sender = chatDto.getSender();
        chat.message = chatDto.getMessage();
        chat.time = chatDto.getTime();

        return chat;
    }
}
