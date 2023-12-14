package com.example.fridge.repository;

import com.example.fridge.entity.Fridge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FridgeRepository extends CrudRepository<Fridge, Long> {
    boolean existsByNameAndOwnerEmail(String name, String ownerEmail);
    Optional<Fridge> findById(String id);

    Optional<Fridge> findByNameAndOwnerEmail(String fridgeName, String ownerEmail);

    Optional<Set<Fridge>> findAllByOwnerEmail(String ownerEmail);

    boolean existsByOwnerEmail(String ownerEmail);
}
