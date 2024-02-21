package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // CartDetailDto
    // CartItem(item) <-> ItemImg(item)
    // 조건1 CartItem(CartId) == 외부 매개변수 CartId
    // 조건2 CartItem(item.id) == ItemImg(item.id)
    // 조건3 ItemImg 대표이미지
    // 정렬 기준 CartItem 등록시간 내림차순
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im "+
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc")
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

    @Query(value = "SELECT * FROM cart_item where item_id=?", nativeQuery = true)
    CartItem findByItemId(@Param("itemId") Long itemId);
    @Query(value = "SELECT * FROM cart_item where reservation_id=?", nativeQuery = true)
    CartItem findByReservationId( Long reservationId);

    @Query(value = "SELECT * FROM cart_item where member_id=?", nativeQuery = true)
    List<CartItem> findByMemberId(@Param("member_id") Long memberId);
}
