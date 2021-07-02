package com.urservices.utils;

import com.urservices.domain.Inscription;
import com.urservices.domain.Requete;
import com.urservices.domain.enumeration.StatutRequete;
import com.urservices.service.dto.NewRequeteDTO;
import java.time.LocalDate;

/**
 * Projet:  pedag
 * Cree par: Brice dymas
 * Date Creation: 2021, Tuesday 08 of June
 */
public class RequeteHelper {

    public static final String getObservationNote(Float moyenne) {
        String obs = "EL";
        if (moyenne != null) {
            if (moyenne <= 9) {
                obs = "NV";
            } else {
                obs = "VA";
            }
        }
        return obs;
    }

    public static Requete newRequeteDtoToRequetePost(NewRequeteDTO param, Inscription inscription) {
        var req = new Requete();
        req.setId(param.getId());
        req.setDateCreation(LocalDate.now());
        req.setDescription(param.getDescription());
        req.setObjet(param.getObjet());
        req.setStatut(StatutRequete.EN_ATTENTE);
        req.setTraiter(false);
        req.setEtudiant(inscription);
        return req;
    }

    public static Requete newRequeteDtoToRequetePatch(NewRequeteDTO param) {
        var req = new Requete();
        req.setId(param.getId());
        req.setDateModification(LocalDate.now());
        req.setDescription(param.getDescription());
        req.setObjet(param.getObjet());
        return req;
    }

    public static NewRequeteDTO requeteToNewRequeteDto(Requete requete) {
        var req = new NewRequeteDTO();
        req.setDescription(requete.getDescription());
        req.setObjet(requete.getObjet());
        req.setId(requete.getId());
        req.setUserId(requete.getEtudiant().getEtudiant().getUser().getId());
        return req;
    }
}
