package com.shop.controller;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final ItemService itemService;

    @GetMapping(value = {"/deluxe", "/deluxe/{page}"})
    public String bread(ItemSearchDto itemSearchDto, Principal principal, Model model,@PathVariable("page") Optional<Integer> page){
        if (itemSearchDto.getSearchRoomType() == null) {
            itemSearchDto.getSearchRoomType();
        }
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "/room/deluxe";
    }
    @GetMapping(value = {"/royal", "/royal/{page}"})
    public String cake(ItemSearchDto itemSearchDto, Principal principal, Model model,@PathVariable("page") Optional<Integer> page){
        if (itemSearchDto.getSearchRoomType() == null) {
         itemSearchDto.getSearchRoomType();
        }
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "/room/royal";
    }
    @GetMapping(value = {"/standard", "/standard/{page}"})
    public String salad(ItemSearchDto itemSearchDto, Principal principal, Model model,@PathVariable("page") Optional<Integer> page){
        if (itemSearchDto.getSearchRoomType() == null) {
            itemSearchDto.getSearchRoomType();
        }
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        return "/room/standard";
    }

    @GetMapping(value ={"/sweet", "/sweet/{page}"})
    public String iceCream(ItemSearchDto itemSearchDto, Principal principal, Model model,@PathVariable("page") Optional<Integer> page){

        if (itemSearchDto.getSearchRoomType() == null) {
          itemSearchDto.getSearchRoomType();
        }

        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);

        model.addAttribute("name",name);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);

        return "/room/sweet";
    }
}
