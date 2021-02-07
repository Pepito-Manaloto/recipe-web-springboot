package com.aaron.recipeweb.entity;

import java.time.LocalDateTime;

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
@Table("recipe")
public class Recipe
{
    @Id
    @Column("id")
    private Integer recipeId;

    @Column
    private String title;

    @Column
    private Integer categoryId;

    @Column("preparation_time")
    private Short preparationTime;

    @Column
    private String description;

    @Column
    private Short servings;

    @Column
    private String author;

    @Column("datein")
    private LocalDateTime createdDate;

    @Column("last_updated")
    private LocalDateTime updatedDate;
}
