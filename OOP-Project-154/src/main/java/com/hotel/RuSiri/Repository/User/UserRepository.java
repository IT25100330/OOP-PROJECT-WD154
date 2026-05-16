package com.hotel.RuSiri.Repository.User;

import com.hotel.RuSiri.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByActiveTrue();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByNic(String nic);


}
