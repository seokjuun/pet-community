package com.potato.petpotatocommunity.controller;

import com.potato.petpotatocommunity.dto.pet.PetDto;
import com.potato.petpotatocommunity.dto.pet.PetUpdateDto;
import com.potato.petpotatocommunity.dto.user.ResponseDto;
import com.potato.petpotatocommunity.dto.user.UserDto;
import com.potato.petpotatocommunity.exception.UnauthorizedException;
import com.potato.petpotatocommunity.service.PetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseDto<List<PetDto>> getMyPet(HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("로그인이 필요합니다.");

        List<PetDto> petList = petService.getMyPet(user.getUserId());
        return ResponseDto.success(petList);
    }


    @PostMapping
    public ResponseDto<PetDto> createMyPet(@RequestBody PetUpdateDto dto, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("로그인이 필요합니다.");
        PetDto newPet = petService.createMyPet(user.getUserId(), dto);
        return ResponseDto.success(newPet);
    }

    @PutMapping("/{petId}")
    public ResponseDto<?> updateMyPet(@PathVariable Long petId,
                                      @RequestBody PetUpdateDto dto,
                                      HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) throw new UnauthorizedException("로그인이 필요합니다.");

        petService.updateMyPet(user.getUserId(), petId, dto);
        return ResponseDto.success("반려동물 정보가 수정되었습니다.");
    }

}
