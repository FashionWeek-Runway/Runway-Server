package com.example.runway.domain;

import com.example.runway.domain.pk.ReviewReadPk;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ReviewRead")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRead {

    @EmbeddedId
    private ReviewReadPk id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false,insertable=false, updatable=false)
    private StoreReview storeReview;


}
