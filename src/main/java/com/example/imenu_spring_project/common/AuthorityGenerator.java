package com.example.imenu_spring_project.common;

import com.example.imenu_spring_project.domain.Authority;
import com.example.imenu_spring_project.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class AuthorityGenerator implements CommandLineRunner {

    private List<String> authorityNameList = List.of("ROLE_USER", "ROLE_ADMIN");

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        long totalCount = authorityRepository.count();
        if(totalCount > 0) return ;

        List<Authority> authorityList = new ArrayList<>();
        for(String name : authorityNameList) {
            Authority authority = Authority.builder()
                    .authorityName(name)
                    .build();
            authorityList.add(authority);
        }

        authorityRepository.saveAll(authorityList);
    }
}
