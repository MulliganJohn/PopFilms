package com.popfilms.popfilmsapi.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "studios")
@NoArgsConstructor
@AllArgsConstructor
public class StudioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "studios")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<MovieEntity> movies = new HashSet<>();

}
