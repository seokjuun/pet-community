package com.potato.petpotatocommunity.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDto {
    private Long petId;
    private String name;
    private String breedName; // CommonCode에서 가져올 이름
    private String groupCodeId;  // ex: "200" (강아지 종)
    private String codeId;       // ex: "001" (사모예드,리트리버 등)
    private String birthday;  // "yyyy-MM-dd" 형식
    private String info;
    private String petImage;
}
