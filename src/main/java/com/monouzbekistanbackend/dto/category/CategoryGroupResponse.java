package com.monouzbekistanbackend.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryGroupResponse {
    private String groupName;
    private List<String> categories;
}
