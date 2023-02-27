package com.example.runway.domain.pk;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewKeepPk implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name="user_id")
    private Long userId;

    @Column(name = "review_id")
    private Long reviewId;
}
