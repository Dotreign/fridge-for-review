package com.example.fridge.repository;

import com.example.fridge.entity.Fridge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FridgeRepository extends CrudRepository<Fridge, Long> {
    boolean existsByNameAndOwnerId(String name, String ownerId);
    Optional<Fridge> findById(String id);

    Optional<Fridge> findByNameAndOwnerId(String fridgeName, String ownerName);
}
