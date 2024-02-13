package com.shop.controller;

import com.shop.constant.ReservationStatus;
import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.Reservation;
import com.shop.repository.ItemRepository;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import com.shop.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {
    private final ItemService itemService;
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final ReservationService reservationService;

    @GetMapping(value = {"/reservations","/reservations/{page}"})
    public String itemReservation(Model model, Principal principal,  @PathVariable("page")Optional<Integer> page,
                                  ReservationDto reservationDto, ItemFormDto itemFormDto, Item item, ItemSearchDto itemSearchDto ){
        String name = memberService.loadMemberName(principal,httpSession);
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        model.addAttribute("name",name);
        System.out.println(itemFormDto);
        ArrayList<MainItemDto> itemList = new ArrayList<>();
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        Page<Item> itemss = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("name",name);
        model.addAttribute("items", items);
        httpSession.setAttribute("checkIn",itemSearchDto.getSearchCheckIn());
        // model.addAttribute("itemss", itemss);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("reservationDto",reservationDto);
        model.addAttribute("maxPage",5);
        return "/item/reservation1";
    }

    @GetMapping(value = "/reservation3/{itemId}")
    public String reservation3(@PathVariable("itemId")Long itemId,ItemSearchDto itemSearchDto, Optional<Integer> page, Model model,
                               Principal principal, ItemFormDto itemFormDto, ReservationDto reservationDto){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        System.out.println(itemFormDto);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        System.out.println(itemFormDto.getCheckIn());
        ItemFormDto itemFormDtoss = itemService.getItemDtl(itemId);
        LocalDateTime date1 = itemSearchDto.getSearchCheckOut().atStartOfDay();
        LocalDateTime date2 = itemSearchDto.getSearchCheckIn().atStartOfDay();
        int betweenDays = (int) Duration.between(date2, date1).toDays();
        System.out.println(itemSearchDto.getSearchCheckIn() + "해당아이템 체크인 날짜");

        model.addAttribute("items", items);
        model.addAttribute("days",betweenDays);
        model.addAttribute("itemId",itemId);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("reservationDto",reservationDto);
        model.addAttribute("item",itemFormDtoss);
        model.addAttribute("ItemFormDto",new ItemFormDto());
        model.addAttribute("firstPrice",itemSearchDto.getSearchPrice());
        //model.addAttribute("prices",reservationDto.getPrice() * betweenDays);

        return "/item/reservation3";
    }

    @PostMapping(value = "/reservationOk")
    public @ResponseBody
    ResponseEntity order(@RequestBody ItemSearchDto itemSearchDto,
                         BindingResult bindingResult,
                         Principal principal, ItemFormDto itemFormDto, Item item,Reservation reservation,OrderDto orderDto,
                         ReservationDto reservationDto,Long id) throws Exception {

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        // 로그인 정보 -> Spring Security
        // principal.getName() (현재 로그인된 정보)
        String email = memberService.loadMemberEmail(principal,httpSession);
        LocalDateTime date1 = itemSearchDto.getSearchCheckOut().atStartOfDay();
        LocalDateTime date2 = itemSearchDto.getSearchCheckIn().atStartOfDay();
        int betweenDays = (int) Duration.between(date2, date1).toDays();
        Long itemId;
        try {
            System.out.println(itemSearchDto.getSearchBreakfast() + "조식인원");
            System.out.println(itemSearchDto.getSearchPrice() + "가격");
            System.out.println(betweenDays + " 날짜 차이");

            System.out.println((itemSearchDto.getSearchPrice()* betweenDays) + (itemSearchDto.getSearchBreakfast() * 20000) + "총 합계");
             reservation = reservationService.reservationOk(id,reservationDto,principal,httpSession,itemFormDto,orderDto,itemSearchDto);
             itemSearchDto.setReservationStatus(ReservationStatus.OK);
            System.out.println(reservation);
            System.out.println("예약완료");
           System.out.println(betweenDays + " 일수차이");

        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
    }


}
