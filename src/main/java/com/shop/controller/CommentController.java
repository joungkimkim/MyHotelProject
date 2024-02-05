package com.shop.controller;

import com.shop.constant.Role;
import com.shop.dto.CommentDto;
import com.shop.entity.Comment;
import com.shop.entity.Member;
import com.shop.service.CommentService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;

@Controller
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
public class CommentController {
    private final HttpSession httpSession;
    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseEntity saveComment(@RequestBody CommentDto commentDto, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
        Comment comment = null;
        try {
            comment = commentService.commentWrite(httpSession, principal, commentDto);
        } catch (Exception e) {
            return new ResponseEntity<String>("댓글 저장중 알 수 없는 에러가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long> (comment.getBoard().getId(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{commentId}")
    @ResponseBody
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Member member = memberService.findMember(httpSession, principal);
        Comment comment;
        try {
            comment = commentService.findComment(commentId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (member.getRole() != Role.ADMIN && !(StringUtils.equals(member.getEmail(), comment.getMember().getEmail()))) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Long boardId = commentService.commentDelete(comment);

        return new ResponseEntity<Long>(boardId, HttpStatus.OK);
    }
    @PatchMapping(value = "/update/{commentId}")
    @ResponseBody
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId, String content, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Member member = memberService.findMember(httpSession, principal);
        Comment comment;
        try {
            comment = commentService.findComment(commentId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (member.getRole() != Role.ADMIN && !(StringUtils.equals(member.getEmail(), comment.getMember().getEmail()))) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Long boardId = commentService.commentUpdate(comment, content);
        return new ResponseEntity<Long>(boardId, HttpStatus.OK);
    }
}
