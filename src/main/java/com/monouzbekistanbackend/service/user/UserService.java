package com.monouzbekistanbackend.service.user;

import com.monouzbekistanbackend.dto.user.*;
import com.monouzbekistanbackend.entity.User;
import com.monouzbekistanbackend.repository.PhoneVerificationRepository;
import com.monouzbekistanbackend.repository.UserRepository;
import com.monouzbekistanbackend.service.IdGeneratorService;
import com.monouzbekistanbackend.service.VerificationHelper;
import com.monouzbekistanbackend.service.VerificationSender;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final IdGeneratorService idGeneratorService;
    private final PasswordEncoder passwordEncoder;
    private final TempUserSave tempUserSave;
    public final Map<String, String> verificationCodeMap = new HashMap<>();
    private final PhoneVerificationRepository phoneVerificationRepository;
    private final VerificationHelper verificationHelper;
    private final VerificationSender verificationSender;

    public UserService(UserRepository userRepository,
                       IdGeneratorService idGeneratorService,
                       PasswordEncoder passwordEncoder,
                       TempUserSave tempUserSave,
                       PhoneVerificationRepository phoneVerificationRepository,
                       VerificationHelper verificationHelper,
                       VerificationSender verificationSender) {
        this.userRepository = userRepository;
        this.idGeneratorService = idGeneratorService;
        this.passwordEncoder = passwordEncoder;
        this.verificationHelper = verificationHelper;
        this.verificationSender = verificationSender;
        this.tempUserSave = tempUserSave;
        this.phoneVerificationRepository = phoneVerificationRepository;
    }

    public UserResponse createUser(UserRequest request) {
        if (request.getFirstName() == null || request.getFirstName().isBlank() ||
                request.getLastName() == null || request.getLastName().isBlank() ||
                request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new RuntimeException("Please fill all the required fields");
        }

        Optional<User> userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userOptional.isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already in use");
        }

        Optional<UserData> tempData = tempUserSave.getTempUser(request.getPhoneNumber());
        if (tempData.isEmpty()) {
            throw new RuntimeException("Telegram user not verified");
        }

        Long telegramUserId = tempData.get().getChatId();

        String userId = idGeneratorService.generateUserId(request.getRole());
        User user = new User();
        user.setUserId(userId);
        user.setTelegramUserId(telegramUserId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setActive(false);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        tempUserSave.remove(request.getPhoneNumber());
        return mapToUserResponseDto(savedUser);

    }

    public UserLoginResponse loginUser(UserLoginRequest request) throws InvalidCredentialsException {
        User user = userRepository.findByUsernameAndPhoneNumber(request.getUsername(), request.getPhoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or phone number"));

        return new UserLoginResponse(user.getUsername(), user.getPhoneNumber(), user.getTelegramUserId());
    }

    public ForgotUsernameResponse forgotUsername(ForgotUsernameRequest request) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Phone number not found");
        }

        User user = userOptional.get();
        verificationSender.sendVerificationCode(user.getPhoneNumber(), user.getTelegramUserId());
        return new ForgotUsernameResponse("Verification code sent");
    }

    public ForgotUsernameResponse resetUsername(ResetUsernameRequest request) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Phone number not found");
        }

        User user = userOptional.get();
        if (user.getUsername().equals(request.getNewUsername())) {
            throw new RuntimeException("Username already in use");
        }

        user.setUsername(request.getNewUsername());
        user.setActive(true);
        userRepository.save(user);
        return new ForgotUsernameResponse("Username has been reset");
    }

    public UserResponse mapToUserResponseDto(User user) {
        UserResponse response = new UserResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUsername(user.getUsername());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        response.setTelegramUserId(user.getTelegramUserId());
        return response;
    }

    public UserResponse getUserByTelegramUserId(Long telegramUserId) {
        Optional<User> optionalUser = userRepository.findByTelegramUserId(telegramUserId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("Telegram user not found");
        }
        return mapToUserResponseDto(optionalUser.get());
    }

    public boolean phoneNumberCheck(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+998\\d{9}$");
    }
}
