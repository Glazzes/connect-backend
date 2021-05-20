package com.connect.repositories;

import com.connect.entities.User;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp(){
        User newUer = new User("Glaze", "Glaze@demo.com", "GlazePassword1");
        underTest.save(newUer);
    }

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    @DisplayName("Given an existing username should return true")
    void givenUsernameShouldReturnTrue(){
        String username = "Glaze";
        boolean exists = underTest.verifyIfUsernameIsAlreadyRegistered(username);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Given a non existing username should return false")
    void giveANonExistingUsernameShouldReturnFalse(){
        String username = "NonExistingUsername";
        boolean exists = underTest.verifyIfUsernameIsAlreadyRegistered(username);
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Give a saved email should return true")
    void givenAnSavedEmailShouldReturnTrue(){
        String email = "Glaze@demo.com";
        boolean exists = underTest.verifyIfEmailIsAlreadyRegistered(email);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Given an email that does exists should return false")
    void givenAnEmailThatDoesNotExistShouldReturnFalse(){
        String email = "some_mail@deom.com";
        boolean exists = underTest.verifyIfEmailIsAlreadyRegistered(email);
        assertThat(exists).isFalse();
    }
    
}
