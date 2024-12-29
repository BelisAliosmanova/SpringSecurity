package com.springSecurity.springSecurity.services;

import com.springSecurity.springSecurity.models.dto.auth.AdminUserDTO;
import com.springSecurity.springSecurity.models.dto.auth.OAuth2UserInfoDTO;
import com.springSecurity.springSecurity.models.dto.auth.PublicUserDTO;
import com.springSecurity.springSecurity.models.dto.auth.RegisterRequest;
import com.springSecurity.springSecurity.models.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(RegisterRequest request);

    User findByEmail(String email);

    List<AdminUserDTO> getAllUsers();

    AdminUserDTO getByIdAdmin(UUID id);

    AdminUserDTO updateUser(UUID id, AdminUserDTO userDTO, PublicUserDTO currentUser);

    void deleteUserById(UUID id, PublicUserDTO currentUser);

    User processOAuthUser(OAuth2UserInfoDTO oAuth2User);

    User findById(UUID id);
}
