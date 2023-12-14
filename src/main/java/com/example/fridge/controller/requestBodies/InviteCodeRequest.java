package com.example.fridge.controller.requestBodies;

public record InviteCodeRequest(String fridgeName, String ownerEmail, String inviteCode) {
}
