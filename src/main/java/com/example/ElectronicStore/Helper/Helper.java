package com.example.ElectronicStore.Helper;

import com.example.ElectronicStore.dto.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    //      U ==> UserEntity
//      V ==> UserResponse
    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {

        List<U> entities = page.getContent();
        List<V> users = entities.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());

        PageableResponse<V> pageableResponse = new PageableResponse<>();

        pageableResponse.setContent(users);
        pageableResponse.setPageNumber(page.getNumber());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setLastPage(page.isLast());
        pageableResponse.setPageSize(page.getNumberOfElements());
        pageableResponse.setTotalElements(page.getTotalElements());

        return pageableResponse;
    }

}
