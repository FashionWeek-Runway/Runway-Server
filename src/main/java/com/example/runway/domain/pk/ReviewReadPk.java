package com.example.runway.domain.pk;

import com.example.runway.domain.StoreReview;
import com.example.runway.domain.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReadPk implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name="user_id")
    private Long userId;

    @Column(name = "review_id")
    private Long reviewId;

}
