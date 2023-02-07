package com.example.runway.service;

import com.example.runway.domain.Category;
import com.example.runway.domain.Store;
import com.example.runway.dto.store.StoreRes;
import com.example.runway.repository.CategoryRepository;
import com.example.runway.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final CategoryRepository categoryRepository;

    public StoreRes.getHomeList getMainHome(Long userId) {

        return null;
    }

    public List<String> getCategoryList(){
        List<Category> category = categoryRepository.findAll();
        return category.stream().map(e-> e.getCategory()).collect(Collectors.toList());
    }
}
