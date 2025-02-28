package com.apexon.catchIt.user.repositroy;


import com.apexon.catchIt.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email,Long id);
}

