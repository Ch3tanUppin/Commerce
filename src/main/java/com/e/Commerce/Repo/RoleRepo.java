package com.e.Commerce.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e.Commerce.Model.AppRole;
import com.e.Commerce.Model.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{

    Optional<Role> findByRoleName(AppRole roleUsers);
    
}
