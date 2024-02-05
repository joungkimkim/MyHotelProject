package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BoardSearchDto {
    private Long id;
    private String searchDateType; // 조회날짜
    private String searchBy; // 조회 유형
    private String searchQuery = ""; // 검색 단어
    private ItemSellStatus searchSellStatus; // 상태
    private String searchTitle;
    private String searchWriter;
    private List<Board> boardList;

    @QueryProjection //Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록  활용
    public BoardSearchDto(Long id,String searchDateType, String searchBy, String searchQuery, ItemSellStatus searchSellStatus, String searchTitle, String searchWriter, List<Board> boardList) {
        this.id = id;
        this.searchDateType = searchDateType;
        this.searchBy = searchBy;
        this.searchQuery = searchQuery;
        this.searchSellStatus = searchSellStatus;
        this.searchTitle = searchTitle;
        this.searchWriter = searchWriter;
        this.boardList = boardList;
    }

    public BoardSearchDto() {

    }
}
