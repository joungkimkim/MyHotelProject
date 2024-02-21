package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.BoardSearchDto;
import com.shop.entity.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public BoardRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체가 만들어 집니다.
    }
    private  BooleanExpression regDtsAfter(String searchDateType){ // all, 1d, 1w, 1m 6m
        LocalDate dateTime = LocalDate.now(); // 현재시간을 추출해서 변수에 대입

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QBoard.board.localDate.after(dateTime);
        //dateTime을 시간에 맞게 세팅 후 시간에 맞는 등록된 상품이 조회하도록 조건값 반환
    }
    private BooleanExpression titleLike(String searchBy, String searchQuery){
        if(StringUtils.equals("title",searchBy)){ // 제목
            return QBoard.board.title.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("writer",searchBy)){ // 작성자
            return QBoard.board.writer.like("%"+searchQuery+"%");
        }
        return null;
    }
    private BooleanExpression titleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.title.like("%"+searchQuery+"%");
    }
    @Override
    public Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QueryResults<Board> results = queryFactory.selectFrom(QBoard.board).
                where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        titleLike(boardSearchDto.getSearchBy(),boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Board> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);

    }
}
