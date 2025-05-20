package com.potato.petpotatocommunity.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetUpdateDto {
    private String name;
    private String breedId; // CommonCode 기준으로 ID 전달
    private String birthday;
    private String info;
    private String petImage;
}
