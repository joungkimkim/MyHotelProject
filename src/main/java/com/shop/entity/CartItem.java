package com.shop.entity;

import com.shop.constant.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

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
    private int price;
    @Column(name = "breakfast")
    private int breakfast;
    private int count;

    public static CartItem createCartItem(Cart cart, Item item,LocalDate checkIn,LocalDate checkOut, int breakfast,
                                          int price, int adultCount, int childrenCount, int count, String type,Member member){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setCheckIn(checkIn);
        cartItem.setCheckOut(checkOut);
        cartItem.setBreakfast(breakfast);
        cartItem.setPrice(price);
        cartItem.setAdultCount(adultCount);
        cartItem.setChildrenCount(childrenCount);
        cartItem.setType(type);
        cartItem.setItem(item);
        cartItem.setReservation(item.getReservation());
        cartItem.setCount(count);
        cartItem.setMember(member);
        return cartItem;
    }
    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
