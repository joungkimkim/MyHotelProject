package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.ReservationStatus;
import com.shop.constant.RoomType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ItemSearchDto {
    private Long itemId;
    private String searchDateType; // 조회날짜
    private ItemSellStatus searchSellStatus; // 상태
    private String searchBy; // 조회 유형
    private String searchQuery = ""; // 검색 단어
    private int searchBreakfast;
    @Min(value = 1, message = "최소 1개이상 담아주세요.")
    private int searchCount;
    private RoomType searchRoomType;
    private String searchRoomNm;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchCheckIn ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchCheckOut ;
    private int searchAdultCount ;
    private int searchChildrenCount ;
    private int searchPrice ;
    private ReservationStatus reservationStatus;

}
