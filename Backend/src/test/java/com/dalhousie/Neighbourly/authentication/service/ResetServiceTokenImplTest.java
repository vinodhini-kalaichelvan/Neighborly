package com.dalhousie.Neighbourly.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dalhousie.Neighbourly.authentication.entity.PasswordReset;
import com.dalhousie.Neighbourly.authentication.repository.PasswordResetTokenRepository;

public class ResetServiceTokenImplTest {
      @InjectMocks
    private ResetTokenServiceImpl resetTokenService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private static final int USER_ID = 1;
    private static final String TOKEN = UUID.randomUUID().toString();
    private static final long MS = 1000L;
    private static final int SEC = 60;
    private static final int MIN = 10;
    private static final Instant EXPIRY_DATE = Instant.now().plusMillis(MS * SEC * MIN);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateResetPasswordToken_NewToken() {

        PasswordReset newPasswordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .build();


        when(passwordResetTokenRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(passwordResetTokenRepository.save(any(PasswordReset.class))).thenReturn(newPasswordReset);


        PasswordReset createdToken = resetTokenService.createResetPasswordToken(USER_ID);


        assertNotNull(createdToken);
        assertEquals(USER_ID, createdToken.getUserId());
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordReset.class));
    }

    @Test
    void testCreateResetPasswordToken_ExistingToken() {
        PasswordReset existingPasswordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token("existingToken")
                .expiryDate(Instant.now().plusMillis(MS * SEC * MIN))
                .build();
        PasswordReset newPasswordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .build();

        when(passwordResetTokenRepository.findByUserId(USER_ID)).thenReturn(Optional.of(existingPasswordReset));
        when(passwordResetTokenRepository.save(any(PasswordReset.class))).thenReturn(newPasswordReset);

        PasswordReset createdToken = resetTokenService.createResetPasswordToken(USER_ID);

        assertNotNull(createdToken);
        assertEquals(USER_ID, createdToken.getUserId());
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordReset.class));
    }

    @Test
    void testFindByUserId() {

        PasswordReset passwordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .build();

        when(passwordResetTokenRepository.findByUserId(USER_ID)).thenReturn(Optional.of(passwordReset));
        Optional<PasswordReset> foundToken = resetTokenService.findByUserId(USER_ID);

        assertTrue(foundToken.isPresent());
        assertEquals(USER_ID, foundToken.get().getUserId());
    }

    @Test
    void testFindByUserId_NotFound() {

        when(passwordResetTokenRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        Optional<PasswordReset> foundToken = resetTokenService.findByUserId(USER_ID);
        assertFalse(foundToken.isPresent());
    }

    @Test
    void testDeleteResetPasswordToken() {

        PasswordReset passwordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .build();

        resetTokenService.deleteResetPasswordToken(passwordReset);
        verify(passwordResetTokenRepository, times(1)).delete(passwordReset);
    }

    @Test
    void testDeleteResetPasswordToken_Null() {

        resetTokenService.deleteResetPasswordToken(null);
        verify(passwordResetTokenRepository, times(0)).delete(any());
    }

    @Test
    void testIsTokenValid_ValidToken() {

        PasswordReset passwordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token(TOKEN)
                .expiryDate(EXPIRY_DATE)
                .build();


        boolean isValid = resetTokenService.isTokenValid(passwordReset);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_ExpiredToken() {

        Instant expiredDate = Instant.now().minusMillis(MS * SEC * MIN);
        PasswordReset expiredPasswordReset = PasswordReset.builder()
                .userId(USER_ID)
                .token(TOKEN)
                .expiryDate(expiredDate)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resetTokenService.isTokenValid(expiredPasswordReset);
        });
        assertEquals("token has expired.", exception.getMessage());
    }

    @Test
    void testIsTokenValid_NullToken() {

        boolean isValid = resetTokenService.isTokenValid(null);
        assertFalse(isValid);
    }
    
}
