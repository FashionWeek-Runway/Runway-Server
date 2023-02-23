package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name="status")
    @ColumnDefault("true")
    private boolean status;
}
