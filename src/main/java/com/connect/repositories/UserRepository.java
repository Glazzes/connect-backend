package com.connect.repositories;

import com.connect.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    @Query("""
    select case when count(u) > 0 then true else false end
    from User u where u.email = :email
    """)
    boolean verifyIfEmailIsAlreadyRegistered(String email);

    @Query("""
    select case when count(u) > 0 then true else false end
    from User u where u.username = :username
    """)
    boolean verifyIfUsernameIsAlreadyRegistered(String username);

}
