package com.urservices.utils;

import com.urservices.domain.Administrateur;
import com.urservices.domain.Enseignant;
import com.urservices.domain.Etudiant;
import com.urservices.security.AuthoritiesConstants;
import com.urservices.service.dto.AdminUserDTO;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Monday 07 of June
 */
public abstract class UserAccountHelper {

    public static final StringBuilder getWelcomeMessage(String name, String login) {
        final StringBuilder sb = new StringBuilder("<br><br>Cher(e) <strong>").append(name).append("</strong>,<br>");
        sb.append("Votre compte a bien ete cree sur notre plateforme. <br> Vos parametres de connexion sont :  <br> <br> Login: <strong>");
        sb.append(login.toLowerCase());
        sb.append("</strong><br><br>Mot de passe: <strong>");
        sb.append(login);
        sb.append("</strong>. <br> <br> <br> <br><strong>Au plaisir de vous revoir,</strong>");
        return sb;
    }

    public static final AdminUserDTO setUpStudentrAccount(Etudiant user) {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setActivated(true);
        userDTO.setAuthorities(new HashSet<>(Collections.singleton(AuthoritiesConstants.STUDENT)));
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getPrenom());
        userDTO.setLastName(user.getNom());
        userDTO.setLogin(user.getMatricule().strip());
        System.out.println("\n \n \n \n");
        System.out.println("Student set is " + userDTO);
        System.out.println("\n \n \n \n");
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
        System.out.println("\n \n \n \n");
        System.out.println("Prof set is " + userDTO);
        System.out.println("\n \n \n \n");
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
