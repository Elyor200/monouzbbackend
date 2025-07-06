package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.dto.category.CategoryGroupResponse;
import com.monouzbekistanbackend.enums.CategoryEnum;
import com.monouzbekistanbackend.enums.category.CategoryGroups;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryGroupService {
    public List<CategoryGroupResponse> GetCategoryGroups() {
        List<CategoryGroupResponse> response = new ArrayList<>();
        response.add(new CategoryGroupResponse("Look",toList(CategoryGroups.Looks)));
        response.add(new CategoryGroupResponse("Sets",toList(CategoryGroups.Sets)));
        response.add(new CategoryGroupResponse("Tops",toList(CategoryGroups.Tops)));
        response.add(new CategoryGroupResponse("Bottoms",toList(CategoryGroups.Bottoms)));
        response.add(new CategoryGroupResponse("Footwear",toList(CategoryGroups.Footwear)));
        response.add(new CategoryGroupResponse("Add_ons",toList(CategoryGroups.Add_ons)));

        return response;
    }

    private List<String> toList(EnumSet<CategoryEnum> group) {
        return group.stream().map(CategoryEnum::toString).collect(Collectors.toList());
    }
}
