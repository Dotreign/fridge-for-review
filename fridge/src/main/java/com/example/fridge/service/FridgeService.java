package com.example.fridge.service;

import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;

public interface FridgeService {
    
    
    String addUser(String fridgeName, String ownerName, String userId);
    
    Fridge getFridge(String fridgeName, String ownerName);


    String removeUser(String fridgeName, String ownerName, String userId);

    Fridge createFridge(FridgeDto fridgeDto);

    String changeInviteCode(String fridgeName, String ownerName, String inviteCode);
}











