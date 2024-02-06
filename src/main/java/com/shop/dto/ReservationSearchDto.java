package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.ItemSellStatus;
import com.shop.constant.RoomType;
import com.shop.entity.Board;
import com.shop.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservationSearchDto {
    private Long id;
    private String searchDateType; // 조회날짜
    private String searchBy; // 조회 유형
    private String searchQuery = ""; // 검색 단어
    private String searchName;
    private String searchEmail;
    private LocalDate searchCheckIn;
    private LocalDate searchCheckOut;
    private RoomType searchRoomType;
    private Integer searchPrice;


    private List<Reservation> reservationList;
    @QueryProjection //Querydsl 결과 조회 시 ReservationSearchDto 객체로 바로 오도록  활용
    public ReservationSearchDto(Long id, String searchDateType, String searchBy, String searchQuery, String searchName,
                                String searchEmail, LocalDate searchCheckIn, LocalDate searchCheckOut, RoomType searchRoomType,
                                Integer searchPrice, List<Reservation> reservationList) {
        this.id = id;
        this.searchDateType = searchDateType;
        this.searchBy = searchBy;
        this.searchQuery = searchQuery;
        this.searchName = searchName;
        this.searchEmail = searchEmail;
        this.searchCheckIn = searchCheckIn;
        this.searchCheckOut = searchCheckOut;
        this.searchRoomType = searchRoomType;
        this.searchPrice = searchPrice;
        this.reservationList = reservationList;
    }

    public ReservationSearchDto() {

    }
}
