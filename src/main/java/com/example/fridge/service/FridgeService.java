package com.example.fridge.service;

import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;
import com.example.fridge.entity.FridgeResponse;

import java.util.Set;

public interface FridgeService {
    
    
    String addUser(String fridgeName, String ownerName, String userId);
    
    Fridge getFridge(String fridgeName, String ownerName);


    String removeUser(String fridgeName, String ownerName, String userId);

    FridgeResponse createFridge(FridgeDto fridgeDto);

    String changeInviteCode(String fridgeName, String ownerName, String inviteCode);

    Set<FridgeResponse> getAllFridges(String ownerEmail);
}











