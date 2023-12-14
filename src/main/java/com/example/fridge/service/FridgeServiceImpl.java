package com.example.fridge.service;

import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;
import com.example.fridge.entity.FridgeResponse;
import com.example.fridge.repository.FridgeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FridgeServiceImpl implements FridgeService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(FridgeServiceImpl.class);

    FridgeRepository fridgeRepository;
    ModelMapper modelMapper = new ModelMapper();
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public FridgeServiceImpl(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }

    public Fridge getFridge(String fridgeName, String ownerEmail) {
        return fridgeRepository.findByNameAndOwnerEmail(fridgeName, ownerEmail)
                .orElseThrow(NoSuchElementException::new);
    }

    public String addUser(String fridgeName, String ownerEmail, String userEmail) {
        Fridge fridge = getFridge(fridgeName, ownerEmail);
        if (fridge.getUsersEmails().contains(userEmail)) {
            throw new IllegalArgumentException("User already in fridge");
        }
        HashMap<String, String> body = new HashMap<>();
        body.put("userEmail", userEmail);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, new HttpHeaders());
        ResponseEntity<Boolean> response = restTemplate
                .exchange("http://gateway:8080/user/checkUser",
                        HttpMethod.POST,
                        request,
                        Boolean.class);
        logger.info(response.getBody().toString());
        if (!response.getBody()) {
            throw new IllegalArgumentException("User not found");
        }
        fridge.getUsersEmails().add(userEmail);
        fridgeRepository.save(fridge);
        return userEmail;
    }

    public String removeUser(String fridgeName, String ownerEmail, String userEmail) {
        Fridge fridge = getFridge(fridgeName, ownerEmail);
        if (!fridge.getUsersEmails().contains(userEmail)) {
            throw new IllegalArgumentException("User not in fridge");
        }
        fridge.getUsersEmails().remove(userEmail);
        fridgeRepository.save(fridge);
        return userEmail;
    }

    public FridgeResponse createFridge(FridgeDto fridgeDto) {
        Fridge fridge = modelMapper.map(fridgeDto, Fridge.class);
        String inviteCode = "";
        for (int i = 0; i < 8; i++) {
            inviteCode += (char) ((int) (Math.random() * 26) + 97);
        }
        fridge.setInviteCode(inviteCode);
        if (!fridgeRepository.existsByOwnerEmail(fridge.getOwnerEmail())) {
            LocalDate premiumUntilDate = LocalDate.now().plusDays(30);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String premiumUntil = premiumUntilDate.format(formatter);
            fridge.setPremiumUntil(premiumUntil);
            fridge.setIsPremium(true);
        } else {
            fridge.setPremiumUntil(null);
            fridge.setIsPremium(false);
        }
        if (fridgeRepository.existsByNameAndOwnerEmail(fridge.getName(), fridge.getOwnerEmail())) {
            throw new IllegalArgumentException("Fridge already exists for owner " + fridge.getOwnerEmail());
        }
        Fridge savedFridge = fridgeRepository.save(fridge);
        return modelMapper.map(savedFridge, FridgeResponse.class);
    }

    public String changeInviteCode(String fridgeName, String ownerEmail, String inviteCode) {
        Fridge fridge = getFridge(fridgeName, ownerEmail);
        fridge.setInviteCode(inviteCode);
        return inviteCode;
    }

    @Override
    public Set<FridgeResponse> getAllFridges(String ownerEmail) {
        Optional<Set<Fridge>> optionalFridges = fridgeRepository.findAllByOwnerEmail(ownerEmail);
        return optionalFridges.map(fridges -> fridges.stream()
                .map(fridge -> modelMapper.map(fridge, FridgeResponse.class))
                .collect(Collectors.toSet())).orElseGet(HashSet::new);
    }

}
