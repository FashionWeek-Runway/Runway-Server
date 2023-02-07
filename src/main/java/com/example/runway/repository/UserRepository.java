package com.example.runway.repository;

import com.example.runway.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String phone);

//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesById(Long userId);





    boolean existsByUsername(String userId);

    boolean existsByNickname(String nickname);


    boolean existsByUsernameAndSocial(String id, String social);



    User findByUsernameAndSocial(String valueOf, String social);


    Optional<User> findByUsername(String phone);


    Optional<User> findByUsernameAndSocialAndStatus(String username, String social, boolean status);
}
