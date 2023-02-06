package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;


@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class User extends BaseEntity implements UserDetails{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="profile_url")
    private String profileImgUrl;

    @Column(name= "username")
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @Column(name="gender")
    private String gender;

    @Column(name="social")
    private String social;

    @Column(name="fcm_token")
    private String fcmToken;

    @Column(name="login_date")
    private LocalDateTime loginDate;

    @Column(name="latitude")
    private double latitude;

    @Column(name="longitude")
    private double longitude;

    @Column(name="alarm")
    private boolean alarm=true;

    // 일반 유저 = 0,  사장님 = 1
    @Builder.Default
    private int owner=0;

    @ColumnDefault("true")
    @Column(name="agree_info")
    private boolean agreeInfo=true;



    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public void updateToken(String fcmToken) {
        this.fcmToken=fcmToken;
    }
    public void updateLogInDate(LocalDateTime now){
        this.loginDate=now;
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}