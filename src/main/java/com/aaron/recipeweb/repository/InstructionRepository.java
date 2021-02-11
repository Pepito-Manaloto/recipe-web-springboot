package com.aaron.recipeweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.aaron.recipeweb.entity.Instruction;

import reactor.core.publisher.Flux;

public interface InstructionRepository extends ReactiveCrudRepository<Instruction, Integer>
{
    Flux<Instruction> findAllByRecipeId(Integer recipeId);
}
