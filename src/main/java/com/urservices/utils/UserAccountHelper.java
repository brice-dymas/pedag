package com.urservices.utils;

import com.urservices.domain.Administrateur;
import com.urservices.domain.Enseignant;
import com.urservices.domain.Etudiant;
import com.urservices.security.AuthoritiesConstants;
import com.urservices.service.dto.AdminUserDTO;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Monday 07 of June
 */
public abstract class UserAccountHelper {

    public static final AdminUserDTO setUpStudentrAccount(Etudiant user) {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setActivated(true);
        userDTO.setAuthorities(new HashSet<>(Collections.singleton(AuthoritiesConstants.STUDENT)));
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getPrenom());
        userDTO.setLastName(user.getNom());
        userDTO.setLogin(user.getMatricule().strip());
        return userDTO;
    }

    public static final AdminUserDTO setUpTeacherAccount(Enseignant user) {
        StringBuilder login = new StringBuilder("prof").append(new Random().nextInt(1000));
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setActivated(true);
        userDTO.setAuthorities(new HashSet<>(Collections.singleton(AuthoritiesConstants.PROF)));
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getPrenom());
        userDTO.setLastName(user.getNom());
        userDTO.setLogin(login.toString());
        return userDTO;
    }

    public static final AdminUserDTO setUpAdminAccount(Administrateur user) {
        StringBuilder login = new StringBuilder("admin").append(new Random().nextInt(1000));
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setActivated(true);
        userDTO.setAuthorities(new HashSet<>(Collections.singleton(AuthoritiesConstants.ADMIN)));
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getPrenom());
        userDTO.setLastName(user.getNom());
        userDTO.setLogin(login.toString());
        return userDTO;
    }
}
