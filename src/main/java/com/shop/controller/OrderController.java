package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.ReservationDto;
import com.shop.entity.Item;
import com.shop.entity.Reservation;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import com.shop.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final ReservationService reservationService;

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal,httpSession, pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "/order/orderHist";
    }
    @GetMapping(value = "/reservationHists")
    public String reservationHist(){


        return "order/reservationHist";
    }




    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){
        if(!orderService.validateOrder(orderId, principal,httpSession)){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
