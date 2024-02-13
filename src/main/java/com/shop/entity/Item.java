package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
@DynamicInsert
public class Item extends BaseEntity{
    @Id //기본키
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동을 1씩 증가
    private Long id; // 상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private Integer price; // 가격

    @Column
    private Integer size;

    @Column(name = "stock_count")
    private int stockNumber; // 재고 수량

    @Lob
    @Column
    private String itemDetail; // 상품 상세 설명

    @Column(name = "type")
    private String roomType; // 방 종류

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    @Column(name = "totalCount")
    private int totalCount;

    @Column(name = "checkIn")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "checkOut")
    private LocalDate checkOut;

//    private LocalDateTime regTime; // 등록 시간
//
//    private LocalDateTime updateTime; // 수정 시간

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_item",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> member;


    public void updateItem(ItemFormDto itemFormDto,ItemSearchDto itemSearchDto){
        this.itemNm = itemFormDto.getItemNm();
        this.roomType= String.valueOf(itemFormDto.getType());
        this.price = itemFormDto.getPrice();
        this.size = itemFormDto.getSize();
        this.stockNumber = itemFormDto.getStockNumber();
        this.totalCount = itemFormDto.getTotalCount();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemSearchDto.getSearchSellStatus();
        this.checkIn = itemFormDto.getCheckIn();
        this.checkOut = itemFormDto.getCheckOut();
    }

    //경고에요 산수 경고 경고
    public void removeStock(int stockNumber){
        ItemFormDto itemFormDto =new ItemFormDto();
        int restStock = this.stockNumber - stockNumber; // 10,  5 / 10, 20
        if (restStock == 0){
            this.itemSellStatus = ItemSellStatus.SOLD_OUT;
        }
        if(restStock<0){
            throw new OutOfStockException("해당 객실은 예약 마감되었습니다.(현재 재고 수량: "+this.stockNumber+")");
        }

        this.stockNumber = restStock; // 5
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

}
