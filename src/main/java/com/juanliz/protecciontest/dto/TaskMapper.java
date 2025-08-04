package com.juanliz.protecciontest.dto;

import com.juanliz.protecciontest.model.Task;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    Task toEntity(TaskCreateDto taskCreateDto);
    Task toEntity(TaskUpdateDto taskUpdateDto);

    TaskCreateDto toCreateDto(Task task);
    TaskUpdateDto toUpdateDto(Task task);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(TaskUpdateDto taskUpdateDto, @MappingTarget Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskCreateDto taskCreateDto, @MappingTarget Task task);
}