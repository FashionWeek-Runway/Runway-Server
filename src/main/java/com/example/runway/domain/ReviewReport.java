package com.example.runway.domain;

import com.example.runway.domain.StoreReview;
import com.example.runway.domain.User;
import com.example.runway.domain.pk.ReviewReadPk;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "ReviewReport")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReport{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false,insertable=false, updatable=false)
    private StoreReview storeReview;

    @Column(name="user_id")
    private Long userId;

    @Column(name = "review_id")
    private Long reviewId;


    @Column(name="reason_id")
    private int reasonId;

    @Column(name="opinion")
    private String opinion;

    @Column(name="status",insertable = false)
    @ColumnDefault(value="true")
    private boolean status;

}
