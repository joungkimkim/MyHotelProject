package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.*;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public Reservation saveReservation(Reservation reservation) {

        return reservationRepository.save(reservation); // 데이터베이스에 저장을 하라는 명령

    }
    public List<Reservation> getmemberIdList(Principal principal , HttpSession httpSession) {

        String email = memberService.loadMemberEmail(principal,httpSession);
        Member member1= memberRepository.findByEmail(email);
        System.out.println(member1.getId());
        Member member2 =memberRepository.findByMemberId(member1.getId());
        return this.reservationRepository.findByMemberId(member2.getId());
    }


    public List<Reservation> getAll()
    {
        return this.reservationRepository.findAll();
    }



    public Reservation getReservationId(@Param("id")Long id) {

        return this.reservationRepository.findByReservationId(id);

    }
    public void deletByeAll() {
        reservationRepository.deleteAll();
    }
    public Reservation reservationOk(ReservationDto reservationDtos,
                                     Principal principal, HttpSession httpSession,ItemFormDto itemFormDto, OrderDto orderDto,ItemSearchDto itemSearchDto) throws Exception {
        String email = memberService.loadMemberEmail(principal,httpSession);
        Member member1= memberRepository.findByEmail(email);
        System.out.println(member1.getId());
        Item item = itemRepository.findById(itemSearchDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member2 =memberRepository.findByMemberId(member1.getId());

        Reservation reservation = Reservation.reservationRoom(reservationDtos,member2,item,itemFormDto,itemSearchDto,principal,httpSession,memberService,reservationDtos.getCount());
        if (reservation == null){
            reservation= Reservation.reservationRoom(reservationDtos,member2,item,itemFormDto,itemSearchDto,principal,httpSession,memberService,reservationDtos.getCount());
            reservationRepository.save(reservation);
        }

        reservationRepository.save(reservation);
        return reservation;

    }

    public Long deleteByMemberId(@Param("reservationId")Long reservationId) {
        reservationRepository.deleteByReservationId(reservationId);
     return reservationId;
    }

    public Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Long reservationDelete(Reservation reservation) {
        Long reservationId = reservation.getId();
        reservationRepository.delete(reservation);
        return reservationId;
    }




}
