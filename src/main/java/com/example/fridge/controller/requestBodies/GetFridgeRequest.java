package com.example.fridge.controller.requestBodies;

import lombok.Data;
import lombok.EqualsAndHashCode;

public record GetFridgeRequest(String fridgeName, String ownerEmail) {
}
