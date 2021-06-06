package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourrielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Courriel.class);
        Courriel courriel1 = new Courriel();
        courriel1.setId(1L);
        Courriel courriel2 = new Courriel();
        courriel2.setId(courriel1.getId());
        assertThat(courriel1).isEqualTo(courriel2);
        courriel2.setId(2L);
        assertThat(courriel1).isNotEqualTo(courriel2);
        courriel1.setId(null);
        assertThat(courriel1).isNotEqualTo(courriel2);
    }
}
