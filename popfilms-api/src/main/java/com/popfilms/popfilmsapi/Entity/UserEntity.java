package com.popfilms.popfilmsapi.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "join_epoch")
    private Long joinEpoch;

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private Set<ReviewEntity> reviews = new HashSet<>();

    public Optional<ReviewEntity> findReviewByMovieId(Long MovieId){
        for (ReviewEntity review : reviews){
            if (review.getMovie().getId().equals(MovieId)){
                return(Optional.of(review));
            }
        }
        return Optional.empty();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<TokenEntity> tokens = new HashSet<>();

    public void addToken(TokenEntity token) {
        tokens.add(token);
        token.setUser(this);
    }

    public void removeAllTokens(){
        tokens.clear();
    }

    @ManyToMany()
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public void addRole(RoleEntity role){
        roles.add(role);
        role.getUsers().add(this);
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (RoleEntity role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

}
