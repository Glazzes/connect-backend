package com.connect.services;

import com.connect.entities.User;
import com.connect.models.SignUpRequest;
import com.connect.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        underTest = new UserService();
        underTest.setUserRepository(userRepository);
        underTest.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void saveNewUserTest(){
        SignUpRequest signUpRequest = new SignUpRequest(
                "Glaze",
                "Glaze@Demo.com",
                "GlazePassword1"
        );

        underTest.createNewUserAccount(signUpRequest);

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(passwordEncoder).encode(passwordCaptor.capture());
        String passwordArgument = passwordCaptor.getValue();
        assertThat(signUpRequest.getPassword()).isEqualTo(passwordArgument);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void findByUsernameTest(){
        String username = "Glaze";
        underTest.findByUsername(username);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByUsername(argumentCaptor.capture());
        String capturedUsername = argumentCaptor.getValue();
        assertThat(username).isEqualTo(capturedUsername);
    }

    @Test
    void verifyIfUsernameIsAlreadyRegisteredTest(){
        String username = "Glaze";
        underTest.verifyIfUsernameIsAlreadyRegistered(username);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).verifyIfUsernameIsAlreadyRegistered(argumentCaptor.capture());
        String capturedUsername = argumentCaptor.getValue();
        assertThat(username).isEqualTo(capturedUsername);
    }

    @Test
    void verifyIfEmailIsAlreadyRegisteredTest(){
        String email = "Glaze@demo.com";
        underTest.verifyIfEmailIsAlreadyRegistered(email);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).verifyIfEmailIsAlreadyRegistered(argumentCaptor.capture());
        String capturedEmail = argumentCaptor.getValue();
        assertThat(email).isEqualTo(capturedEmail);
    }

}
