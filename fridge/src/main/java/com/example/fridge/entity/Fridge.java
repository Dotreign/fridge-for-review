package com.example.fridge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "ownerId"})})
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
    @Column(nullable = false, name = "ownerId")
    String ownerId;
    @ElementCollection
    Set<String> usersIds;
    @Column(nullable = false, name = "inviteCode")
    String inviteCode;

}
