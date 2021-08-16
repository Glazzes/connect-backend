package com.connect.services;

import com.connect.user.domain.entities.PostgresUser;
import com.connect.user.domain.model.SignUpRequest;
import com.connect.user.domain.repository.PostgresUserRepository;
import com.connect.user.application.service.PostgresUserService;
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
public class PostgresPostgresUserServiceTest {
    private PostgresUserService underTest;

    @Mock
    private PostgresUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        underTest = new PostgresUserService();
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

        verify(userRepository).save(any(PostgresUser.class));
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
        underTest.existsByEmail(username);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).existsByUsername(argumentCaptor.capture());
        String capturedUsername = argumentCaptor.getValue();
        assertThat(username).isEqualTo(capturedUsername);
    }

    @Test
    void verifyIfEmailIsAlreadyRegisteredTest(){
        String email = "Glaze@demo.com";
        underTest.existsByEmail(email);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).existsByEmail(argumentCaptor.capture());
        String capturedEmail = argumentCaptor.getValue();
        assertThat(email).isEqualTo(capturedEmail);
    }

}
