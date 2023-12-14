package com.example.fridge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "ownerEmail"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false, name = "name")
    String name;
    @Column(nullable = false, name = "isPremium")
    Boolean isPremium;
    @Column(nullable = false, name = "ownerEmail")
    String ownerEmail;
    @ElementCollection
    Set<String> usersEmails;
    @Column(nullable = false, name = "inviteCode")
    String inviteCode;
    @Column(name = "premiumUntil")
    String premiumUntil;

}
