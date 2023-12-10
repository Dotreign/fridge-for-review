package com.example.repository;

import com.example.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserEntityRepository extends ReactiveCrudRepository<UserEntity, String> {

    Mono<UserEntity> findByEmail(String email);

}
