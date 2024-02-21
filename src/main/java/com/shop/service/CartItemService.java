package com.shop.service;

import com.shop.entity.CartItem;
import com.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItem findByItemId(@PathVariable("itemId") Long itemId){
      return cartItemRepository.findByItemId(itemId);

    }
    public List<CartItem> findByMemberId(Long memberId){
      return  cartItemRepository.findByMemberId(memberId);
    }
    public CartItem findByReservationId(Long reservationId){
        return cartItemRepository.findByReservationId(reservationId);

    }
}
