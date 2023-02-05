package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "Store")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Store extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @Column(name="user_id",columnDefinition = "게시한 사용자")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "time")
    private String time;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "website")
    private String website;

    @Column(name="latitude")
    private double latitude;

    @Column(name="longitude")
    private double longitude;



}
