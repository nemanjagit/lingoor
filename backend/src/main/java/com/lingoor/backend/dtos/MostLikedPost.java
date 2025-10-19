package com.lingoor.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MostLikedPost {
    private String word;
    private String definition;
    private String author;
    private long likeCount;
}
