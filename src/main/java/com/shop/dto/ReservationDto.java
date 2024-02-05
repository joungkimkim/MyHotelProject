package com.shop.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.shop.constant.RoomType;
import com.shop.entity.Item;
import com.shop.entity.Reservation;
import com.shop.repository.ReservationRepository;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ReservationDto {

        private Long itemId;

        private int adultCount;

        private int childrenCount;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate checkIn;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate checkOut;

        private String type;

        private Integer price;

        private Integer breakfast;
        private int count;

        private List<ReservationDto> reservationDtoList;




}
