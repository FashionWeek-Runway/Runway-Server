package com.example.runway.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Social")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Social extends BaseEntity{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,insertable=false, updatable=false)
    private User user;

    @Column(name="user_id")
    private Long userId;

    @Column(name="social_id")
    private String socialId;

    @Column(name="type")
    private String type;
}
