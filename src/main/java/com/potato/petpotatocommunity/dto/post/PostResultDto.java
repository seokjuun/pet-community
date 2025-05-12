package com.potato.petpotatocommunity.dto.post;

import lombok.Data;

import java.util.List;

@Data
public class PostResultDto {
    private String result;
    private PostDto postDto;
    private List<PostDto> postDtoList;
    private Long count;
}
