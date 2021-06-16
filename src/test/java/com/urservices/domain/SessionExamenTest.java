package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionExamenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionExamen.class);
        SessionExamen sessionExamen1 = new SessionExamen();
        sessionExamen1.setId(1L);
        SessionExamen sessionExamen2 = new SessionExamen();
        sessionExamen2.setId(sessionExamen1.getId());
        assertThat(sessionExamen1).isEqualTo(sessionExamen2);
        sessionExamen2.setId(2L);
        assertThat(sessionExamen1).isNotEqualTo(sessionExamen2);
        sessionExamen1.setId(null);
        assertThat(sessionExamen1).isNotEqualTo(sessionExamen2);
    }
}
