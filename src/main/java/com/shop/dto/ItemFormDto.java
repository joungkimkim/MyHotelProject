package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.RoomType;
import com.shop.entity.Item;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    // Item
    private Long id;

    @NotBlank(message = "객실명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "객실 상세정보는 필수 입력 값입니다.")
    private  String itemDetail;

    @NotNull(message = "객실 개수는 필수 입력 값입니다.")
    private Integer stockNumber;

    //@NotNull(message = "객실 크기는 필수 입력 값입니다.")
    private Integer size;

    @NotNull(message = "전체인원은 필수 입력 값입니다.")
    private Integer totalCount;

    private ItemSellStatus itemSellStatus;

    private RoomType type;

    private int adultCount;

    private int childrenCount;


    private LocalDate checkIn;

    private LocalDate checkOut;

    //----------------------------------------------------------------------------
    //ItemImg
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 이미지 정보

    private List<Long> itemImgIds = new ArrayList<>(); //상품 이미지 아이디

//--------------------------------------------------------------------------------------
    // ModelMapper
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        // ItemFormDto -> Item 연결
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item){
        // Item -> ItemFormDto 연결
        return modelMapper.map(item, ItemFormDto.class);
    }
}
