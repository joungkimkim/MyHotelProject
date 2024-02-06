package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.ReservationSearchDto;
import com.shop.entity.Item;
import com.shop.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationCustom {
    Page<Reservation> getAdminReservationPage(ReservationSearchDto reservationSearchDto, Pageable pageable);
}
