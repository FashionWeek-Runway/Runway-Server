package com.example.runway.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Region")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region extends BaseEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "region")
    private String region;

    @Column(name= "address")
    private String address;
}
