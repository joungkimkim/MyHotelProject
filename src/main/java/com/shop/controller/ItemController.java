package com.shop.controller;
import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.Reservation;
import com.shop.repository.ItemRepository;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import com.shop.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final ReservationService reservationService;
    private final ItemRepository itemRepository;


    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "/item/itemForm";
    }
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid  ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Principal principal, HttpServletRequest request, HttpServletResponse response,
                          ReservationDto reservationDto){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("reservationDto",reservationDto);
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage",
                    "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        model.addAttribute("itemName",itemFormDto.getItemNm());
        model.addAttribute("price",reservationDto.getPrice());
        System.out.println(reservationDto.getPrice() + " 예약 Dto 가격");
        httpSession.setAttribute("itemNm",itemFormDto.getItemNm());

        return "redirect:/";
    }
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        httpSession.setAttribute("itemId",itemId);
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            httpSession.setAttribute("itemId",itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
           // return "item/itemForm";
        }
        httpSession.setAttribute("itemId",itemId);
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model, Principal principal, ItemSearchDto itemSearchDto, Reservation reservation){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList,itemSearchDto,reservation);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; // 다시 실행 /
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page,
                             Model model, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId, Principal principal){
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);

        model.addAttribute("item",itemFormDto);
        return "item/itemDtl";
    }

}
