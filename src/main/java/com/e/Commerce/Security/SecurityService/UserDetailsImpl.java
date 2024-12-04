package com.e.Commerce.Security.SecurityService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.e.Commerce.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *  customizing user detail implementation
 */

@NoArgsConstructor
@Data
//this class implements the interface of user details
public class UserDetailsImpl implements UserDetails{
    private static final long serialVersionUID = 1L; //this line is to ensure to consistency across diff jvm

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    //below is the collection of roles and permission that is grandted to users
    private Collection<? extends GrantedAuthority> authorities;

    //constructor
    public UserDetailsImpl(Long id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /*
     * below code is returing user details type and accpeting user
     * it converts domain user object
     * the model user is getting converted to UserDetailsImpl type
     */
    public static UserDetailsImpl build(User user){
        //getting the auth, roles and permission which user have 
        List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());

        //then returing the new object 
        return new UserDetailsImpl(user.getUserId(), user.getUserName(), user.getEmail(), user.getPassword(), authorities);
    }

    //these method are overridden and getter and setters
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    //it compares the user with id attribute
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass() != o.getClass()) return false;

        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
   
}
