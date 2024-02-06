package com.shop.service;


import com.shop.dto.ApproveResponse;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.OrderDto;
import com.shop.dto.ReadyResponse;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public ReadyResponse payReady(@PathVariable("itemId")Long itemId) {
        // 카카오가 요구한 결제요청request값을 담아줍니다.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        ItemSearchDto itemSearchDto =new ItemSearchDto();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("itemId", String.valueOf(itemId));
        parameters.add("partner_order_id", "아이템");
        parameters.add("partner_user_id", "inflearn");
        parameters.add("item_name", "itemName");
        parameters.add("quantity", "1");
        parameters.add("searchCheckIn", String.valueOf(itemSearchDto.getSearchCheckIn()));
        parameters.add("searchCheckOut", String.valueOf(itemSearchDto.getSearchCheckOut()));
        parameters.add("searchBreakfast", String.valueOf(itemSearchDto.getSearchBreakfast()));
        parameters.add("searchAdultCount", String.valueOf(itemSearchDto.getSearchAdultCount()));
        parameters.add("searchChildrenCount", String.valueOf(itemSearchDto.getSearchChildrenCount()));
        parameters.add("searchPrice", String.valueOf(itemSearchDto.getSearchPrice()));
        parameters.add("searchRoomType", String.valueOf(itemSearchDto.getSearchRoomType()));
        parameters.add("total_amount", "3000");
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", "http://localhost/kakao/pay/completed/"+ itemId); // 결제승인시 넘어갈 url
        parameters.add("cancel_url", "http://localhost/kakao/pay/cancel"); // 결제취소시 넘어갈 url
        parameters.add("fail_url", "http://localhost/kakao/pay/fail"); // 결제 실패시 넘어갈 url
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부url요청 통로 열기.
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
        ReadyResponse readyResponse = template.postForObject(url, requestEntity, ReadyResponse.class);
        // 받아온 값 return
        return readyResponse;
    }

    // 결제 승인요청 메서드
    public ApproveResponse payApprove(@PathVariable("itemId")Long itemId,
                                      String tid, String pgToken,OrderDto orderDto ,Principal principal ,
                                      HttpSession httpSession, ItemSearchDto itemSearchDto) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("tid", tid);
        parameters.add("partner_order_id", "아이템"); // 주문명
        parameters.add("partner_user_id", "inflearn");
        parameters.add("pg_token", pgToken);
        parameters.add("itemId", String.valueOf(itemId));
        parameters.add("searchCheckIn", String.valueOf(itemSearchDto.getSearchCheckIn()));
        parameters.add("searchCheckOut", String.valueOf(itemSearchDto.getSearchCheckOut()));
        parameters.add("searchBreakfast", String.valueOf(itemSearchDto.getSearchBreakfast()));
        parameters.add("searchAdultCount", String.valueOf(itemSearchDto.getSearchAdultCount()));
        parameters.add("searchChildrenCount", String.valueOf(itemSearchDto.getSearchChildrenCount()));
        parameters.add("searchRoomType", String.valueOf(itemSearchDto.getSearchRoomType()));
        parameters.add("searchPrice", String.valueOf(itemSearchDto.getSearchPrice()));
       // parameters.add("order_id", String.valueOf(id));
        // 하나의 map안에 header와 parameter값을 담아줌.
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부url 통신
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스.
        ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
        //log.info("결재승인 응답객체: " + approveResponse);

        return approveResponse;
    }
    // header() 셋팅
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK a1b2a9e9627095fe1e6ebb3d28f81ad4");
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }

}
