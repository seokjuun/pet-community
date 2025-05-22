package com.potato.petpotatocommunity.dto.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDto {
    private String groupCode;
    private String code;
    private String codeName;
}

