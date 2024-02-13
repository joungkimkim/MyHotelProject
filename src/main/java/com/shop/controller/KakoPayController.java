package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Reservation;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.ReservationRepository;
import com.shop.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/kakao")
@RequiredArgsConstructor

public class KakoPayController {
private final ItemService itemService;
private final ItemRepository itemRepository;
private final MemberService memberService;
private final ReservationService reservationService;
private final ReservationRepository reservationRepository;
private final OrderService orderService;
private final MemberRepository memberRepository;
private final OrderRepository orderRepository;
private final KakaoPayService kakaoPayService;
    // 카카오페이결제 요청
    @GetMapping("/pay/{itemId}")
    public @ResponseBody ReadyResponse payReady( ItemSearchDto itemSearchDto, Model model, @PathVariable("itemId")Long itemId
                                                 ,HttpSession httpSession) {
        // 카카오 결제 준비하기	- 결제요청 service 실행.
        model.addAttribute("itemSearchDto",itemSearchDto);
        ReadyResponse readyResponse = kakaoPayService.payReady(itemId);
        ItemFormDto itemFormDtoss = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDtoss);
        // 요청처리후 받아온 결재고유 번호(tid)를 모델에 저장
        //model.addAttribute("tid", readyResponse.getTid());
       // log.info("결재고유 번호: " + readyResponse.getTid());
        // Order정보를 모델에 저장
       // model.addAttribute("order",order);
        String type = String.valueOf(itemSearchDto.getSearchRoomType());
        LocalDateTime date1 = itemSearchDto.getSearchCheckOut().atStartOfDay();
        LocalDateTime date2 = itemSearchDto.getSearchCheckIn().atStartOfDay();
        int betweenDays = (int) Duration.between(date2, date1).toDays();
        int price =itemSearchDto.getSearchPrice();
        int count = itemSearchDto.getSearchCount();
        int breakfast = itemSearchDto.getSearchBreakfast();
        int totalPrice = (price * betweenDays) * count + (breakfast* 20000);

        httpSession.setAttribute("checkIn",itemSearchDto.getSearchCheckIn());
        httpSession.setAttribute("checkOut",itemSearchDto.getSearchCheckOut());
        httpSession.setAttribute("count",itemSearchDto.getSearchCount());
        httpSession.setAttribute("adultCount",itemSearchDto.getSearchAdultCount());
        httpSession.setAttribute("childCount",itemSearchDto.getSearchChildrenCount());
        httpSession.setAttribute("breakfast",itemSearchDto.getSearchBreakfast());
        httpSession.setAttribute("price",itemSearchDto.getSearchPrice());
        httpSession.setAttribute("type",type);
        httpSession.setAttribute("totalPrice",totalPrice);
        return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
    }

    // 결제승인요청
    @GetMapping(value = "/pay/completed/{itemId}")
    public String  approveResponse( @PathVariable("itemId")Long itemId, ItemSearchDto itemSearchDto,
                                   Principal principal, HttpSession httpSession, ItemFormDto itemFormDto, OrderDto orderDto, ReservationDto reservationDtos,Model model) throws Exception {
        String email=memberService.loadMemberEmail(principal,httpSession);
        reservationService.reservationOk(itemId,reservationDtos,principal,httpSession,itemFormDto,orderDto,itemSearchDto);
        //System.out.println(reservation);
        System.out.println("결제 완료");
        return "redirect:/";
    }
    // 결제 취소시 실행 url
    @GetMapping("/pay/cancel")
    public String payCancel() {
        System.out.println("취소");
        return "redirect:/";
    }

    // 결제 실패시 실행 url
    @GetMapping("/pay/fail")
    public String payFail() {
        System.out.println("실패");
        return "redirect:/";
    }

}
