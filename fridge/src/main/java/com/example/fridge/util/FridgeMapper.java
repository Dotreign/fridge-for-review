package com.example.fridge.util;

import com.example.fridge.entity.Fridge;
import com.example.fridge.entity.FridgeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FridgeMapper {
    FridgeDto toDto(Fridge fridge);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inviteCode", ignore = true)
    Fridge toEntity(FridgeDto fridgeDto);
}