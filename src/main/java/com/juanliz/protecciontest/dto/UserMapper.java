package com.juanliz.protecciontest.dto;

import com.juanliz.protecciontest.model.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserGetDto userDto);
    User toEntity(UserUpdateDto userUpdateDto);

    UserGetDto toGetDto(User user);
    UserUpdateDto toUpdateDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserUpdateDto userUpdateDto, @MappingTarget User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserGetDto userDto, @MappingTarget User user);
}