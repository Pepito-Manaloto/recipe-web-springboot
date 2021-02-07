package com.aaron.recipeweb.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.aaron.recipeweb.entity.User;

public interface UserRepository extends ReactiveCrudRepository<User, String>
{

}
