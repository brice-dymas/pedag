package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequeteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Requete.class);
        Requete requete1 = new Requete();
        requete1.setId(1L);
        Requete requete2 = new Requete();
        requete2.setId(requete1.getId());
        assertThat(requete1).isEqualTo(requete2);
        requete2.setId(2L);
        assertThat(requete1).isNotEqualTo(requete2);
        requete1.setId(null);
        assertThat(requete1).isNotEqualTo(requete2);
    }
}
