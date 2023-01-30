package com.example.imenu_spring_project.repository;

import com.example.imenu_spring_project.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesById(Long userId);


    User findByUsername(String username);

    User findByUsernameEquals(String username);

    //User findByid(Long userId);

    boolean existsByUsername(String userId);

    boolean existsByNickname(String nickname);

    User findByNameAndPhone(String name, String phone);

    boolean existsByUsernameAndSocial(String id, String social);



    User findByUsernameAndSocial(String valueOf, String social);
}
