package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.RoomType;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.ReservationSearchDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QReservation;
import com.shop.entity.Reservation;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationCustomImpl implements ReservationCustom{
    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public ReservationCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체가 만들어 집니다.
    }
    private BooleanExpression regDtsAfter(String searchDateType){ // all, 1d, 1w, 1m 6m
        LocalDateTime dateTime = LocalDateTime.now(); // 현재시간을 추출해서 변수에 대입

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QReservation.reservation.reservationTime.after(LocalDate.from(dateTime));
        //dateTime을 시간에 맞게 세팅 후 시간에 맞는 등록된 상품이 조회하도록 조건값 반환
    }
    private BooleanExpression searchRoomTypeEq(RoomType searchRoomType) {
        return searchRoomType == null ? null : QReservation.reservation.type.eq(String.valueOf(searchRoomType));
    }
    private BooleanExpression searchCheckInEq(LocalDate searchCheckIn) {
        return searchCheckIn == null ? null : QReservation.reservation.checkIn.eq(searchCheckIn);
    }
    private BooleanExpression searchCheckOutEq(LocalDate searchCheckOut) {
        return searchCheckOut == null ? null : QReservation.reservation.checkOut.eq(searchCheckOut);
    }
    private BooleanExpression searchPriceEq(Integer searchPrice) {
        return searchPrice == null ? null : QReservation.reservation.price.eq(searchPrice);
    }



    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("email",searchBy)){ // 작성자
            return QReservation.reservation.email.like("%"+searchQuery+"%");
        }
        else if(StringUtils.equals("name",searchBy)){ // 작성자
            return QReservation.reservation.name.like("%"+searchQuery+"%");
        }
        else if(StringUtils.equals("type",searchBy)){ // 작성자
            return QReservation.reservation.type.like("%"+searchQuery+"%");
        }

        else {

            return null;
        }
    }
    @Override
    public Page<Reservation> getAdminReservationPage(ReservationSearchDto reservationSearchDto, Pageable pageable) {

        QueryResults<Reservation> results = queryFactory.selectFrom(QReservation.reservation).
                where(regDtsAfter(reservationSearchDto.getSearchDateType()),
                        searchRoomTypeEq(reservationSearchDto.getSearchRoomType()),
                        searchCheckInEq(reservationSearchDto.getSearchCheckIn()),
                        searchCheckOutEq(reservationSearchDto.getSearchCheckOut()),
                        searchPriceEq(reservationSearchDto.getSearchPrice()),
                        searchByLike(reservationSearchDto.getSearchBy(),reservationSearchDto.getSearchQuery()))
                .orderBy(QReservation.reservation.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Reservation> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
}
