package com.shop.controller;

import com.shop.dto.BoardSearchDto;
import com.shop.dto.CommentDto;
import com.shop.dto.WriteFormDto;
import com.shop.entity.Board;
import com.shop.entity.Item;
import com.shop.repository.BoardRepository;
import com.shop.repository.CommentRepository;
import com.shop.repository.MemberRepository;
import com.shop.service.BoardService;
import com.shop.service.CommentService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FaqController {
private final MemberService memberService;
private final BoardService boardService;
private final BoardRepository boardRepository;
private final CommentRepository commentRepository;
private final CommentService commentService;
private final MemberRepository memberRepository;
private final HttpSession httpSession;


    @GetMapping(value = {"/boardLists","/boardLists/{page}"})
    public String boardList(Model model, @PathVariable("page") Optional<Integer> page, Principal principal, BoardSearchDto boardSearchDto) {
        //Page<Board> boardList = boardService.getBoardList(page);
        //model.addAttribute("boardList", boardList);
        String name = memberService.loadMemberName(principal, httpSession);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 41);
        Page<Board> boards = boardService.getBoardPage(boardSearchDto, pageable);
        model.addAttribute("name", name);
        model.addAttribute("maxPage", 5);
        model.addAttribute("boards",boards);
        model.addAttribute("boardSearchDto",new BoardSearchDto());
        return "/faq/board";
    }

    @GetMapping(value = "/write")
    public String writeForm(Model model, WriteFormDto writeFormDto, Principal principal) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        model.addAttribute("writeFormDto", writeFormDto);
        return "/faq/writeBoardForm";
    }

    @PostMapping(value = "/write")
    public String write(@Valid WriteFormDto writeFormDto, BindingResult bindingResult,
                        Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            String name = memberService.loadMemberName(principal, httpSession);
            model.addAttribute("name", name);
            return "/faq/writeBoardForm";
        }
        Board board = boardService.writeBoard(writeFormDto, principal, httpSession);
        boardRepository.save(board);
        return "redirect:/faq/boardLists";
    }

    @GetMapping(value = "/detail/{id}")
    public String userBoardDetail(Model model, @PathVariable("id") Long boardId, Principal principal) {
        try {
            String name = memberService.loadMemberName(principal, httpSession);
            WriteFormDto writeFormDto = WriteFormDto.of(boardService.getId(boardId));

            List<CommentDto> mainCommentList = commentService.mainCommentList(boardId);
            List<CommentDto> subCommentList = commentService.subCommentList(boardId);

            model.addAttribute("name", name);
            model.addAttribute("writeFormDto", writeFormDto);

            model.addAttribute("mainCommentList", mainCommentList);
            model.addAttribute("subCommentList", subCommentList);
        } catch (Exception e) {
            System.out.println("~~~~~~~~~~~~~~상세보기 에러~~~~~~~~~~~~~~~~~~");
            return "redirect:/faq/boardLists";
        }
        return "/faq/userBoardDetail";
    }

    @GetMapping(value = "/boardDetail/{id}")
    public String boardDetail(Model model, @PathVariable("id") Long boardId, Principal principal) {
        WriteFormDto writeFormDto = WriteFormDto.of(boardService.getId(boardId));
        String name = memberService.loadMemberName(principal, httpSession);
        System.out.println(writeFormDto);
        model.addAttribute("writeFormDto", writeFormDto);
        model.addAttribute("name", name);
        return "/faq/boardDetail";
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid WriteFormDto writeFormDto, BindingResult bindingResult, @PathVariable("id") Long boardId,
                         Model model, Principal principal) {
        String name = memberService.loadMemberName(principal, httpSession);
        model.addAttribute("name", name);
        if (bindingResult.hasErrors()) {
            return "/faq/boardDetail";
        }
        boardService.BoardAs(writeFormDto, boardId);
        return "redirect:/faq/boardLists";
    }

    @PostMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Long boardId, Model model) {
        boardService.deleteById(boardId);
        model.addAttribute("id", boardId);
        return "redirect:/faq/boardLists";
    }
}
