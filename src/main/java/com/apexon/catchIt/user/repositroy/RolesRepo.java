package com.apexon.catchIt.user.repositroy;


import com.apexon.catchIt.user.model.Role;
import com.apexon.catchIt.user.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
@Repository
public interface RolesRepo extends JpaRepository<Role,Long> {
   Set<Role> findByRoleNameIn(Set<Roles> names);
    Optional<Role>findByRoleName(Roles roleName);
}
