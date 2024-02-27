package com.popfilms.popfilmsapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "directors")
@NoArgsConstructor
@AllArgsConstructor
public class DirectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "directors")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<MovieEntity> movies = new HashSet<>();

}
