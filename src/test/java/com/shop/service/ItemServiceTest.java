package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;

    // 가상으로 List<MultipartFile> 생성한다.
    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for(int i =0;i<5;i++){
            String path = "C:/shop/item";
            String imageName = "image"+i+".jpg";
            // MockMultipartFile 가상으로 MultipartFile 생성
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName,
                            "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }
    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws  Exception{
        //ItemFormDto 객체 생성 후 데이터 적재
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        // createMultipartFiles 호출해서
        // 가상의 List<MultipartFile> 만든 걸 변수 multipartFileList에 대입

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto,multipartFileList);

        // 저장을 하면 itemId를 이용해서 List<ItemImg> 데이터를 받아서 itemImgList에 대입
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 저장을 하면 itemId를 이용해서 Item 데이터를 받아서 item에 대입
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        //ItemFromDto 내용과 Item 내용을 비교해서 같은지 확인
        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        //multipartFileList->0번째인덱스 OriginalFilename == itemImgList -> 0번째 인덱스 OriImgName 같은지 확인
        assertEquals(multipartFileList.get(0).getOriginalFilename(),
                itemImgList.get(0).getOriImgName());
    }
}