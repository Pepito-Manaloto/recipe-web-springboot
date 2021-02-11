package com.aaron.recipeweb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("instructions")
public class Instruction
{
    @Id
    @Column("recipe_id")
    @JsonIgnore
    private Integer recipeId;

    @Column
    private String instruction;
}
