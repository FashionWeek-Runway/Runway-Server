package com.example.runway.domain;

import com.example.runway.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "StoreImg")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class StoreImg extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable=false, updatable=false)
    private Store store;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="store_img")
    private String storeImg;

    @Column(name="sequence")
    private int sequence;
}
