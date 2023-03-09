package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "StoreReview")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class StoreReview extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,insertable=false, updatable=false)
    private Store store;

    @Column(name="store_id")
    private Long storeId;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name="status",insertable = false)
    @ColumnDefault(value="true")
    private boolean status;

    @Column(name="deleted",insertable = false)
    @ColumnDefault(value="true")
    private boolean deleted;

    public void modifyStatus(boolean status) {
        this.status=status;
    }

    public void unActive(boolean b) {
        this.status = b;
    }

    public void delete(boolean b) {
        this.deleted=b;
    }
}
