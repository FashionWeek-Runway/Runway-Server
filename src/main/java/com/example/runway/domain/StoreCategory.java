package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
@Entity
@Table(name = "StoreCategory")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class StoreCategory extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false,insertable=false, updatable=false)
    private Store store;

    @Column(name="store_id")
    private Long storeId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false,insertable=false, updatable=false)
    private Category category;

    @Column(name="category_id")
    private Long categoryId;

    @Column(name="status")
    @ColumnDefault("true")
    private boolean status;
}
