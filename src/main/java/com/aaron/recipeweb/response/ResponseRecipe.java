package com.aaron.recipeweb.response;

import java.util.List;

import com.aaron.recipeweb.entity.Ingredient;
import com.aaron.recipeweb.entity.Instruction;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRecipe
{
    private String title;
    private String category;

    @JsonProperty("preparation_time")
    private Short preparationTime;

    private String description;
    private Short servings;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;
}
