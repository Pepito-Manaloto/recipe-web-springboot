package com.aaron.recipeweb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCount
{
    private String category;
    private String author;
    private Integer count;
}
