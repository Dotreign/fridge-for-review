package com.example.fridge.service;

import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;
import com.example.fridge.repository.FridgeRepository;
import com.example.fridge.util.FridgeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FridgeServiceImpl implements FridgeService {

    FridgeRepository fridgeRepository;
    FridgeMapper fridgeMapper;
    RestTemplate restTemplate;

    @Autowired
    public FridgeServiceImpl(FridgeRepository fridgeRepository, FridgeMapper fridgeMapper) {
        this.fridgeRepository = fridgeRepository;
        this.fridgeMapper = fridgeMapper;
    }

    public Fridge getFridge(String fridgeName, String ownerName) {
        return fridgeRepository.findByNameAndOwnerId(fridgeName, ownerName)
                .orElseThrow(NoSuchElementException::new);
    }

    public String addUser(String fridgeName, String ownerName, String userId) {
        Fridge fridge = getFridge(fridgeName, ownerName);
        if (fridge.getUsersIds().contains(userId)) {
            throw new IllegalArgumentException("User already in fridge");
        }
        fridge.getUsersIds().add(userId);
        fridgeRepository.save(fridge);
        return userId;
    }

    public String removeUser(String fridgeName, String ownerName, String userId) {
        Fridge fridge = getFridge(fridgeName, ownerName);
        if (!fridge.getUsersIds().contains(userId)) {
            throw new IllegalArgumentException("User not in fridge");
        }
        fridge.getUsersIds().remove(userId);
        fridgeRepository.save(fridge);
        return userId;
    }

    public Fridge createFridge(FridgeDto fridgeDto) {
        Fridge fridge = fridgeMapper.toEntity(fridgeDto);
        // generate random 8-character  invite code
        String inviteCode = "";
        for (int i = 0; i < 8; i++) {
            inviteCode += (char) ((int) (Math.random() * 26) + 97);
        }
        fridge.setInviteCode(inviteCode);
        if (fridgeRepository.existsByNameAndOwnerId(fridge.getName(), fridge.getOwnerId())) {
            throw new IllegalArgumentException("Fridge already exists for owner " + restTemplate.getForObject("http://gateway:8080/user/" + fridge.getOwnerId(), String.class));
        }
        return fridgeRepository.save(fridge);
    }

    public String changeInviteCode(String fridgeName, String ownerName, String inviteCode) {
        Fridge fridge = getFridge(fridgeName, ownerName);
        fridge.setInviteCode(inviteCode);
        return inviteCode;
    }

}
