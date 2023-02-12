package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "StoreBlog")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class StoreBlog extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",insertable=false, updatable=false)
    private Store store;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="blog_title")
    private String blogTitle;

    @Column(name="blog_url")
    private String blogUrl;

    @Column(name="thumbnail_url")
    private String thumbnailUrl;


}
