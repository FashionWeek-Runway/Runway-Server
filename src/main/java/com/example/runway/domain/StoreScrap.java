package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "StoreScrap")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class StoreScrap extends BaseEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable=false, updatable=false)
    private Store store;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="img_url")
    private String imgUrl;

    @Column(name="img_cnt")
    private int imgCnt;

    private String title;

    @Column(name="web_url")
    private String webUrl;

    private String content;
}
