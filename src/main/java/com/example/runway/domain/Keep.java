package com.example.runway.domain;

import com.example.runway.domain.pk.KeepPk;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "Keep")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Keep extends BaseEntity {

    @EmbeddedId
    private KeepPk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,insertable=false, updatable=false)
    private Store store;


    @Column(name="status",insertable = false)
    @ColumnDefault(value="true")
    private boolean status;
}
