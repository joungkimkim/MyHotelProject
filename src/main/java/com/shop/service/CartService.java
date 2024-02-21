package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.*;
import com.shop.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.util.StringUtils;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;
    private final MemberService memberService;
    private final ReservationRepository reservationRepository;

    public Long addCart(ItemSearchDto itemSearchDto, String email, Long itemId, LocalDate checkIn, LocalDate checkOut, int breakfast,
                        int price, int adultCount, int childrenCount, int count, String type){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityExistsException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(),itemId);
        if(savedCartItem != null){
            savedCartItem.addCount(count);
            return savedCartItem.getId();
        }
        else{
            CartItem cartItem = CartItem.createCartItem(cart, item, checkIn, checkOut,  breakfast,
             price,  adultCount,  childrenCount,  count,  type,member);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(Principal principal, HttpSession httpSession){
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        String email =  memberService.loadMemberEmail(principal,httpSession);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());

        System.out.println(member.getId() +" member id 조회 CartService");
        if(cart == null){
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(@PathVariable("itemId") Long itemId, Principal principal, HttpSession httpSession){
        String email = memberService.loadMemberEmail(principal,httpSession);
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findByItemId(itemId);
        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(curMember.getEmail(),savedMember.getEmail())){
            return false;
        }
        return true;
    }
    public void updateCartItemCount(@PathVariable("itemId") Long itemId, int count){
        CartItem cartItem = cartItemRepository.findByItemId(itemId);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(@PathVariable("itemId") Long itemId){
        CartItem cartItem = cartItemRepository.findByItemId(itemId);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }
        Long orderId = orderService.orders(orderDtoList, email);

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }

    public void saveCart(Member member) {
    }
}
