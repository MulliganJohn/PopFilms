package com.popfilms.popfilmsapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tokens")
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(name = "token_value")
    private String tokenValue;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
