package com.monouzbekistanbackend.service.user;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TempUserSave {
    private final Map<String, UserData>  tempUserMap = new ConcurrentHashMap<>();

    public void saveTempUser(String phoneNumber, UserData userData) {
        tempUserMap.put(phoneNumber, userData);
    }

    public Optional<UserData> getTempUser(String phoneNumber) {
        return Optional.ofNullable(tempUserMap.get(phoneNumber));
    }

    public void remove(String phoneNumber) {
        tempUserMap.remove(phoneNumber);
    }
}