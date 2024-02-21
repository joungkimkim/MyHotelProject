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

    private int price;
    @Column(name = "breakfast")
    private int breakfast;

    private int stockNumber;

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



    public static Reservation reservationRoom(Member member, Item item, Principal principal, HttpSession httpSession, MemberService memberService,
                                              String type,int price,int breakfast, int stockNumber,int adultCount,int childrenCount,LocalDate checkIn,LocalDate checkOut ) {
        Reservation reservation = new Reservation();
        String email = memberService.loadMemberEmail(principal, httpSession);
        String name = memberService.loadMemberName(principal, httpSession);
//        LocalDateTime date1 = checkIn.atStartOfDay();
//        LocalDateTime date2 = checkOut.atStartOfDay();
//        int betweenDays = (int) Duration.between(date2, date1).toDays();

        // 카카오페이
            reservation.setMember(member);
            reservation.setType(type);
            reservation.setEmail(email);
            reservation.setName(name);
            reservation.setBreakfast(breakfast);
            reservation.setAdultCount(adultCount);
            reservation.setChildrenCount(childrenCount);
            reservation.setStockNumber(stockNumber);
            reservation.setCheckIn(checkIn);
            reservation.setCheckOut(checkOut);
            reservation.setReservationTime(LocalDate.now());
            reservation.setItem(item);
            reservation.setPrice(price);
            reservation.setReservationStatus(ReservationStatus.OK.getStringValue());

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
