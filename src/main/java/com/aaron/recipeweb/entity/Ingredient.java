package com.aaron.recipeweb.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ingredients")
public class Ingredient
{
    @Id
    @Column("recipe_id")
    private Integer recipeId;

    @Column
    private BigDecimal quantity;

    @Column
    private String measurement;

    @Column
    private String ingredient;

    @Column("comment_")
    private String comment;
}
