package com.juanliz.protecciontest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignUsersRequest {
    private List<Integer> userIds;
}
