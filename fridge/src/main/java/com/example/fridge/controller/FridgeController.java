package com.example.fridge.controller;

import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;
import com.example.fridge.service.FridgeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/")
public class FridgeController {

    FridgeServiceImpl fridgeService;

    @Autowired
    public FridgeController(FridgeServiceImpl fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping
    public ResponseEntity<Fridge> getFridge(@RequestBody String fridgeName, @RequestBody String ownerName) {
        try {
            return ResponseEntity.ok(fridgeService.getFridge(fridgeName, ownerName));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody String fridgeName, @RequestBody String ownerName, @RequestBody String userId) {
        try {
            return ResponseEntity.ok(fridgeService.addUser(fridgeName, ownerName, userId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/removeUser")
    public ResponseEntity<String> removeUser(@RequestBody String fridgeName, @RequestBody String ownerName, @RequestBody String userName) {
        try {
            return ResponseEntity.ok(fridgeService.removeUser(fridgeName, ownerName, userName));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Fridge> createFridge(@RequestBody FridgeDto fridgeDto) {
        try {
            return ResponseEntity.ok(fridgeService.createFridge(fridgeDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("changeInviteCode")
    public ResponseEntity<String> changeInviteCode(@RequestBody String fridgeName, @RequestBody String ownerName, @RequestBody String inviteCode) {
        try {
            return ResponseEntity.ok(fridgeService.changeInviteCode(fridgeName, ownerName, inviteCode));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
