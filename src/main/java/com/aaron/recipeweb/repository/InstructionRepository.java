package com.aaron.recipeweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.aaron.recipeweb.entity.Instruction;

public interface InstructionRepository extends ReactiveCrudRepository<Instruction, Integer>
{

}
