package com.aaron.recipeweb.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRecipeListWrapper
{
    private int recentlyAddedCount;
    private List<ResponseRecipe> recipes;
}
