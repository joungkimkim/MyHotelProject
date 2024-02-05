package com.shop.dto;

import com.shop.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CommentDto {
    private Long id;

    private Long boardId;
    private Long parentId;
    private String memberName;

    private String content;

    private LocalDateTime regTime;
    private String upTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public Comment createComment() {
        return modelMapper.map(this, Comment.class);
    }
    public static CommentDto of(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setUpTime(comment.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        commentDto.setMemberName(comment.getMember().getName());
        if (comment.getParent() != null) {
            commentDto.setParentId(comment.getParent().getId());
        }
        return modelMapper.map(comment, CommentDto.class);
    }
    public void timeSetting() {
        this.upTime = getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
