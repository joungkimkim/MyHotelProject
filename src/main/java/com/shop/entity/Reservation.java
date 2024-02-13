package com.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.constant.ItemSellStatus;
import com.shop.constant.ReservationStatus;
import com.shop.constant.Role;
import com.shop.dto.*;
import com.shop.exception.OutOfStockException;
import com.shop.service.MemberService;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import javassist.runtime.Desc;
import lombok.Getter;
import lombok.Setter;
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
    @OrderBy(value = "reservation_id DESC")
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

    private String reservationStatus;
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



    public static Reservation reservationRoom(Long itemId,ReservationDto reservationDtos, Member member,
                                              Item item,ItemFormDto itemFormDto,ItemSearchDto itemSearchDto,
                                              Principal principal, HttpSession httpSession, MemberService memberService,int count) {
        Reservation reservation = new Reservation();
        ReservationSearchDto reservationSearchDto= new ReservationSearchDto();
        String email = memberService.loadMemberEmail(principal, httpSession);
        String name = memberService.loadMemberName(principal, httpSession);
        // 카카오페이
         if (itemSearchDto.getSearchCheckIn() == null || itemSearchDto.getSearchCheckOut() == null) {
            LocalDate sessionCheckIn = (LocalDate) httpSession.getAttribute("checkIn");
            LocalDate sessionCheckOut = (LocalDate) httpSession.getAttribute("checkOut");
            int adultCount =(int) httpSession.getAttribute("adultCount");
           int childCount = (int) httpSession.getAttribute("childCount");
           int breakfast = (int) httpSession.getAttribute("breakfast");
           int counts = (int) httpSession.getAttribute("count");
             int totalPrice = (int) httpSession.getAttribute("totalPrice");
            String type =(String) httpSession.getAttribute("type");
            Integer price = (Integer) httpSession.getAttribute("price");
            reservation.setMember(member);
            reservation.setPrice(totalPrice);
            reservation.setEmail(email);
            reservation.setName(name);
            reservation.setCheckIn(sessionCheckIn);
            reservation.setCheckOut(sessionCheckOut);
            reservation.setAdultCount(adultCount);
            reservation.setChildrenCount(childCount);
            reservation.setBreakfast(breakfast);
            reservation.setReservationTime(LocalDate.now());
            reservation.setType(type);
            reservation.setReservationStatus(ReservationStatus.OK.getStringValue());
            item.removeStock(counts);
        }
        // 일반 예약
        else {
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
            reservation.setPrice((itemSearchDto.getSearchPrice()));
            reservation.setReservationStatus(ReservationStatus.OK.getStringValue());
            item.removeStock(itemSearchDto.getSearchCount());
        }
        return reservation;
    }
    public static Reservation update(Long reservationId) {
        Reservation reservation=new Reservation();
        reservation.reservationStatus= ReservationStatus.CANCEL.getStringValue();
    return reservation;
    }
    public void cancel(Long reservationId,String cancel,ReservationStatus reservationStatus){
        //this.getItem().addStock(itemSearchDto.getSearchCount());
        this.reservationStatus= String.valueOf(reservationStatus);

    }

    public void updateStatus(Long reservationId){
        this.reservationStatus = ReservationStatus.CANCEL.getStringValue();
    }


}
