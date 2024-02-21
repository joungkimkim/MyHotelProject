package com.shop.entity;

import com.shop.dto.WriteFormDto;
import com.shop.service.MemberService;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(columnDefinition = "varchar(1000)")
    private String content;
    private String writer;
    private LocalDate localDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(value = "comment_id ASC")
    private List<Comment> commentList = new ArrayList<>();      // 댓글

    public static Board writeBoard(WriteFormDto boardWriteFormDto, Member member, MemberService memberService,
                                   Principal principal, HttpSession httpSession){
        Board board=new Board();
        String name = memberService.loadMemberName(principal,httpSession);
        board.setTitle(boardWriteFormDto.getTitle());
        board.setContent(boardWriteFormDto.getContent());
        board.setLocalDate(LocalDate.now());
        board.setWriter(name);
        board.setMember(member);
        return board;
    }
    public Board boardAs(WriteFormDto writeFormDto) {
        this.title = writeFormDto.getTitle();
        this.content = writeFormDto.getContent();
        return this;
    }




}
