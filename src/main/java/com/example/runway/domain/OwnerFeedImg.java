package com.example.runway.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "OwnerFeedImg")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class OwnerFeedImg extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false,insertable=false, updatable=false)
    private OwnerFeed ownerFeed;

    @Column(name= "feed_id")
    private Long feedId;

    @Column(name="img_url")
    private String imgUrl;

    @Column(name="status",insertable = false)
    @ColumnDefault(value="true")
    private boolean status;

}
