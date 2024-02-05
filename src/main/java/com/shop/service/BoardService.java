package com.shop.service;


import com.shop.dto.BoardSearchDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MemberASDto;
import com.shop.dto.WriteFormDto;
import com.shop.entity.Board;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.BoardRepository;
import com.shop.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service // 나 서비스다.
@Transactional // 트랜젝션설정 : 성공을하면 그대로 적용 실패하면 롤백
@RequiredArgsConstructor // final 또는 @NonNull 명령어가 붙으면 객체를 자동 붙혀줍니다.
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    public Board saveBoard(Board board) {
        return boardRepository.save(board); // 데이터베이스에 저장을 하라는 명령
    }
    public Board writeBoard(WriteFormDto writeFormDto, Principal principal , HttpSession httpSession) {

        String email= memberService.loadMemberEmail(principal,httpSession);

        Member member = memberRepository.findByEmail(email);

        Member member1 = memberRepository.findByMemberId(member.getId());

        Board board =  Board.writeBoard(writeFormDto,member1,memberService,principal,httpSession);

        if (board==null){
            board = Board.writeBoard(writeFormDto,member1,memberService,principal,httpSession);
            boardRepository.save(board);
        }
        boardRepository.save(board);
        return board;

    }
    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    public Board getId(Long boardId) {
        return boardRepository.findByBoardId(boardId);
    }

    public void BoardAs(WriteFormDto writeFormDto,@Param("id") Long id) {
        Board board = boardRepository.findByBoardId(id);
        board =board.boardAs(writeFormDto);
        boardRepository.save(board);
    }
    public void deleteById(@Param("id") Long id) {
        boardRepository.delete(
                boardRepository.findById(id).orElseThrow()
        );
    }
    @Transactional(readOnly = true)
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
        return boardRepository.getBoardPage(boardSearchDto,pageable);
    }
    public Page<Board> getBoardList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10,Sort.by(Sort.Direction.DESC,"id")); // <- Sort 추가
        return boardRepository.findAll(pageable);
    }



}
