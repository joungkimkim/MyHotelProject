package com.shop.controller;

import com.shop.constant.RoomType;
import com.shop.dto.*;
import com.shop.entity.CartItem;
import com.shop.entity.Member;
import com.shop.entity.Reservation;
import com.shop.repository.*;
import com.shop.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
private final CartItemService cartItemService;
private final KakaoPayService kakaoPayService;
private final CartService cartService;
private final CartItemRepository cartItemRepository;
    // 카카오페이결제 요청
    @GetMapping("/pay/{itemId}")
    public @ResponseBody ReadyResponse payReady( ItemSearchDto itemSearchDto, Model model, @PathVariable("itemId")Long itemId
                                                 ,HttpSession httpSession,LocalDate searchCheckIn,LocalDate searchCheckOut,Integer searchPrice,
                                                 Integer searchCount,Integer searchAdultCount,Integer searchChildrenCount,
                                                 Integer searchBreakfast,String searchRoomType) {
        // 카카오 결제 준비하기	- 결제요청 service 실행
        RoomType type= (RoomType) httpSession.getAttribute("type");
        model.addAttribute("itemSearchDto",itemSearchDto);

        if (searchCount == null || searchAdultCount == null || searchChildrenCount == null) {
            CartItem cartItem = cartItemService.findByItemId(itemId);
            searchCheckIn= cartItem.getCheckIn();
            searchCheckOut = cartItem.getCheckOut();
            searchCount = cartItem.getCount();
            searchAdultCount = cartItem.getAdultCount();
            searchChildrenCount = cartItem.getChildrenCount();
            searchPrice= cartItem.getPrice();
            searchRoomType = cartItem.getType();
            searchBreakfast= 1;
            ReadyResponse readyResponse = kakaoPayService.payReady(itemId, httpSession, searchRoomType, searchPrice, searchCount,searchBreakfast, searchAdultCount, searchChildrenCount, searchCheckIn, searchCheckOut);
            return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)

        }
        LocalDate checkIn = (LocalDate) httpSession.getAttribute("checkIn");
        LocalDate checkOut = (LocalDate) httpSession.getAttribute("checkOut");
        int count = (int) httpSession.getAttribute("count");
        int adultCount = (int) httpSession.getAttribute("adultCount");
        int childCount = (int) httpSession.getAttribute("childCount");
        int breakfast = (int) httpSession.getAttribute("breakfast");
        int price = (int) httpSession.getAttribute("price");
            ReadyResponse readyResponse = kakaoPayService.payReady(itemId, httpSession, searchRoomType, price, count,breakfast, adultCount, childCount, checkIn, checkOut);
            return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)

    }

    // 결제승인요청
    @GetMapping(value = "/pay/completed/{itemId}")
    public String  approveResponse( @PathVariable("itemId")Long itemId,
                                   Principal principal, HttpSession httpSession,CartItem cartItem1,ItemSearchDto itemSearchDto) throws Exception {
        String email=memberService.loadMemberEmail(principal,httpSession);
             cartItem1 =cartItemRepository.findByItemId(itemId);
             if (cartItem1 == null || httpSession.getAttribute("count") == null){
                 LocalDate checkIn = itemSearchDto.getSearchCheckIn();
                 LocalDate checkOut = itemSearchDto.getSearchCheckOut();
                 int price = itemSearchDto.getSearchPrice();
                 int breakfast = itemSearchDto.getSearchBreakfast();
                 int count = itemSearchDto.getSearchCount();
                 int adultCount = itemSearchDto.getSearchAdultCount();
                 int childCount = itemSearchDto.getSearchChildrenCount();
                 RoomType type= (RoomType) httpSession.getAttribute("type");
                reservationService.reservationOk(itemId,principal,httpSession,
                         String.valueOf(type), price,  breakfast,  count,  adultCount, childCount, checkIn, checkOut);
                 System.out.println( " 카트 삭제 안됨");
                 return "redirect:/";
             }
             else{
                 LocalDate checkIn = (LocalDate) httpSession.getAttribute("checkIn");
                 LocalDate checkOut = (LocalDate) httpSession.getAttribute("checkOut");
                 int count = (int)  httpSession.getAttribute("count");
                 int adultCount = (int)   httpSession.getAttribute("adultCount");
                 int childCount = (int) httpSession.getAttribute("childCount");
                 int breakfast = (int)httpSession.getAttribute("breakfast");
                 int price =(int) httpSession.getAttribute("price");
                 RoomType type= (RoomType) httpSession.getAttribute("type");
             reservationService.reservationOk(itemId,principal,httpSession,
                         String.valueOf(type), price,  breakfast,  count,  adultCount, childCount, checkIn, checkOut);
                 cartItemRepository.deleteById(cartItem1.getId());
                 System.out.println("해당 카트 삭제");

             }
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
