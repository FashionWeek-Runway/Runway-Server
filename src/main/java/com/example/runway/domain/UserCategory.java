package com.example.runway.domain;

import com.example.runway.domain.pk.UserCategoryPk;
import io.grpc.netty.shaded.io.netty.handler.codec.socksx.v4.Socks4CommandRequest;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "UserCategory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
@DynamicInsert
public class UserCategory extends BaseEntity {

    @EmbeddedId
    private UserCategoryPk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,insertable=false, updatable=false)
    private Category category;

    @Column(name="status",insertable = false)
    @ColumnDefault(value="true")
    private boolean status;



}
