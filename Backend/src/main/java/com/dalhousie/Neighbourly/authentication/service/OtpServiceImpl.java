package com.dalhousie.Neighbourly.authentication.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dalhousie.Neighbourly.authentication.entity.Otp;
import com.dalhousie.Neighbourly.authentication.repository.OtpRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;

    private static final int OTP_MIN_VALUE = 100000;
    private static final int OTP_RANGE = 900000;
    private final long EXPIRATION_DURATION = 1000L * 60 * 10;

    @Transactional
    @Override
    public Otp generateOtp(Integer userId) {
        Optional<Otp> existingOtp = otpRepository.findByUserId(userId);
        existingOtp.ifPresent(this::deleteOtp);

        String otpValue = String.valueOf(OTP_MIN_VALUE + new Random().nextInt(OTP_RANGE));

        Otp otp = Otp.builder()
                .otp(otpValue)
                .expiryDate(Instant.now().plusMillis(EXPIRATION_DURATION))
                .userId(userId)
                .build();

        return otpRepository.save(otp);
    }

    public Otp resendOtp(Integer userId) {
        Optional<Otp> existingOtp = otpRepository.findByUserId(userId);
        existingOtp.ifPresent(this::deleteOtp);
        return generateOtp(userId);
    }

    @Transactional
    @Override
    public void deleteOtp(Otp otp) {
        if (otp != null) {
            otpRepository.deleteByUserId(otp.getUserId());
        }
    }


    @Override
    public Optional<Otp> findByOtp(String otpValue) {
        return otpRepository.findByOtp(otpValue);
    }

    public boolean isOtpValid(Otp otp) {
        if (otp == null || otp.getExpiryDate() == null) {
            return false;
        }
        if (otp.getExpiryDate().isBefore(Instant.now())) {
            throw new TokenExpiredException("otp has expired.");
        }

        return true;
    }

    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String message) {
            super(message);
        }
    }
    
}


