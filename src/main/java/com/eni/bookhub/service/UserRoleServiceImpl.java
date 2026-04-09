package com.eni.bookhub.service;

import com.eni.bookhub.BO.UserRole;
import com.eni.bookhub.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserRoleServiceImpl {
    private UserRoleRepository userRoleRepository;

    public Optional<UserRole> getUserRoleById(Long id){
        return userRoleRepository.getUserRoleById(id);
    }
    public Optional<UserRole> getUserRoleByName(String name) {
        return userRoleRepository.getUserRoleByName(name);
    }
    public List<UserRole> getAllUserRole() {
        return userRoleRepository.findAll();
    }
}
