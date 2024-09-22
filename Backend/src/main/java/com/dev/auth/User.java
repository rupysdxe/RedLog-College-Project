package com.dev.auth;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Rupesh Dangi
 * @date: 2024/09/08 20/19
 */
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    @Column(unique = true)
    @Email
    private String username;
    @NotEmpty
    @JsonIgnore
    private String password;
    @ColumnDefault("1")
    private boolean isAccountNonExpired=true;
    @ColumnDefault("1")
    private boolean isAccountNonLocked=true;
    @ColumnDefault("1")
    private boolean isCredentialsNonExpired=true;
    @ColumnDefault("1")
    private boolean isEnabled=true;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @JsonIgnore
    public UserDetails getUserDetails() {
      return new UserDetails() {
          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
              return Collections.singletonList((GrantedAuthority) () -> role.name());
          }
          @Override
          public String getPassword() {
              return password;
          }

          @Override
          public String getUsername() {
              return username;
          }

          @Override
          public boolean isAccountNonExpired() {
              return isAccountNonExpired;
          }

          @Override
          public boolean isAccountNonLocked() {
              return isAccountNonLocked;
          }

          @Override
          public boolean isCredentialsNonExpired() {
              return isCredentialsNonExpired;
          }

          @Override
          public boolean isEnabled() {
              return isEnabled;
          }
      };
    }
}
