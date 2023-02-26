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
public class UserCategoryPk implements Serializable {
    private static final long serialVersionUID = 1L;


    @Column(name="user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long category_id;
}
