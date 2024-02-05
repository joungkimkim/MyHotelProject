package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext // Entity 영속성 저장 환경
    EntityManager em;
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }
    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member); // 테이이블 저장 준비

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); //영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 flush() 호출하여 데이터베이스에 반영
        em.clear(); //영속성 컨텍스트에 조회 후 엔티티가 없을 경우 데이터 베이스를 조회 영속성 컨텍스트를 비워줍니다.
        // Member 테이블에 저장 되고 Cart 테이블에 저장 됩니다.
        /*
            findById select * from cart where id = ? ? => id
            위 쿼리문에서 문제가 발생하면 실행 -> EntityNotFoundException Throw
            orElseThrow ->  EntityNotFoundException(결과 X)
            위 쿼리문에서 문제가 없으면 -> Cart(Entity)
         */
        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        // 비교 savedCart -> member -> id == member -> id
        // 같으면 OK 같지 않거나 이상하면 Fail
        assertEquals(savedCart.getMember().getId(), member.getId());
    }
}