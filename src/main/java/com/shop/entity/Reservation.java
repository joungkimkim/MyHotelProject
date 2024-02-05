package com.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.dto.*;
import com.shop.service.MemberService;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation {
    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int adultCount;
    private int childrenCount;

    @Column(name = "checkIn")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "checkOut")
    private LocalDate checkOut;

    @Column(name = "type")
    private String type;

    @Column(name = "email")
    private String email;

    private String name;

    private Integer price;
    @Column(name = "breakfast")
    private Integer breakfast;

    private Integer stockNumber;

    @Column(name = "reservationTime")
    private LocalDate reservationTime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    @JsonManagedReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 외래키
    private Item item;



    public static Reservation reservationRoom(ReservationDto reservationDtos, Member member,
                                              Item item,ItemFormDto itemFormDto,ItemSearchDto itemSearchDto,
                                              Principal principal, HttpSession httpSession, MemberService memberService,int count) {
        Reservation reservation = new Reservation();
        String email = memberService.loadMemberEmail(principal, httpSession);
        String name = memberService.loadMemberName(principal, httpSession);
        // 카카오페이
        if (itemSearchDto.getSearchCheckIn() == null || itemSearchDto.getSearchCheckOut() == null) {
            LocalDate sessionCheckIn = (LocalDate) httpSession.getAttribute("checkIn");
            LocalDate sessionCheckOut = (LocalDate) httpSession.getAttribute("checkOut");
            LocalDateTime SessionCheckIn = sessionCheckIn.atStartOfDay();
            LocalDateTime SessionCheckOut = sessionCheckOut.atStartOfDay();
            int sessionBetweenDays = (int) Duration.between(SessionCheckIn, SessionCheckOut).toDays();
            reservation.setMember(member);
            reservation.setPrice(sessionBetweenDays);
            reservation.setEmail(email);
            reservation.setName(name);
            reservation.setCheckIn(sessionCheckIn);
            reservation.setCheckOut(sessionCheckOut);
            reservation.setAdultCount(itemSearchDto.getSearchAdultCount());
            reservation.setChildrenCount(itemSearchDto.getSearchChildrenCount());
            reservation.setBreakfast(itemSearchDto.getSearchBreakfast());
        }
        // 일반 예약
        else {
            LocalDateTime checkIn = itemSearchDto.getSearchCheckIn().atStartOfDay();
            LocalDateTime checkOut = itemSearchDto.getSearchCheckOut().atStartOfDay();
            int breakfast = itemSearchDto.getSearchBreakfast() * 20000;
            int sessionBetweenDays = (int) Duration.between(checkIn, checkOut).toDays();
            reservation.setMember(member);
            reservation.setType(String.valueOf(itemSearchDto.getSearchRoomType()));
            reservation.setEmail(email);
            reservation.setName(name);
            reservation.setBreakfast(itemSearchDto.getSearchBreakfast());
            reservation.setAdultCount(itemSearchDto.getSearchAdultCount());
            reservation.setChildrenCount(itemSearchDto.getSearchChildrenCount());
            reservation.setStockNumber(reservationDtos.getCount());
            reservation.setCheckIn(itemSearchDto.getSearchCheckIn());
            reservation.setCheckOut(itemSearchDto.getSearchCheckOut());
            reservation.setReservationTime(LocalDate.now());
            reservation.setItem(item);
            reservation.setPrice((itemSearchDto.getSearchPrice() * sessionBetweenDays) + breakfast);
            item.removeStock();
        }
        return reservation;
    }
    public void cancel(){
        this.getItem().addStock(stockNumber);
    }

}
