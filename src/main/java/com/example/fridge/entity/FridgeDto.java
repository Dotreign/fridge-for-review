package com.example.fridge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FridgeDto {

    String name;
    Boolean isPremium = false;
    String ownerEmail;
    Set<String> usersEmails = Set.of();

}
