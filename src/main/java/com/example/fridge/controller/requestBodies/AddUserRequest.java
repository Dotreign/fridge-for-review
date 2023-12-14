package com.example.fridge.controller.requestBodies;

public record AddUserRequest(String fridgeName, String ownerEmail, String userEmail) {
}
