package com.example.fridge.controller;

import com.example.fridge.controller.requestBodies.AddUserRequest;
import com.example.fridge.controller.requestBodies.GetAllFridgesRequest;
import com.example.fridge.controller.requestBodies.GetFridgeRequest;
import com.example.fridge.controller.requestBodies.InviteCodeRequest;
import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;
import com.example.fridge.entity.FridgeResponse;
import com.example.fridge.service.FridgeServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/fridge")
public class FridgeController {

    FridgeServiceImpl fridgeService;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public FridgeController(FridgeServiceImpl fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping
    public ResponseEntity<FridgeResponse> getFridge(@RequestBody GetFridgeRequest request) {
        try {
            Fridge fridge = fridgeService.getFridge(request.fridgeName(), request.ownerEmail());
            return ResponseEntity.ok(modelMapper.map(fridge, FridgeResponse.class));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getAll")
    public ResponseEntity<Set<FridgeResponse>> getAllFridges(@RequestBody GetAllFridgesRequest ownerEmail) {
        try {
            return ResponseEntity.ok(fridgeService.getAllFridges(ownerEmail.ownerEmail()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody AddUserRequest request) {
        try {
            return ResponseEntity.ok(fridgeService.addUser(request.fridgeName(), request.ownerEmail(), request.userEmail()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/removeUser")
    public ResponseEntity<String> removeUser(@RequestBody AddUserRequest request) {
        try {
            return ResponseEntity.ok(fridgeService.removeUser(request.fridgeName(), request.ownerEmail(), request.userEmail()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<FridgeResponse> createFridge(@RequestBody FridgeDto fridgeDto) {
        try {
            return ResponseEntity.ok(fridgeService.createFridge(fridgeDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("changeInviteCode")
    public ResponseEntity<String> changeInviteCode(@RequestBody InviteCodeRequest request) {
        try {
            return ResponseEntity.ok(fridgeService.changeInviteCode(request.fridgeName(), request.ownerEmail(), request.inviteCode()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
