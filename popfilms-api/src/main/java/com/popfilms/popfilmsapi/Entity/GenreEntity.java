package com.popfilms.popfilmsapi.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<MovieEntity> movies = new HashSet<>();

}
