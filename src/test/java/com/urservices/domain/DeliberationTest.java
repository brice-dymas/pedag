package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliberationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deliberation.class);
        Deliberation deliberation1 = new Deliberation();
        deliberation1.setId(1L);
        Deliberation deliberation2 = new Deliberation();
        deliberation2.setId(deliberation1.getId());
        assertThat(deliberation1).isEqualTo(deliberation2);
        deliberation2.setId(2L);
        assertThat(deliberation1).isNotEqualTo(deliberation2);
        deliberation1.setId(null);
        assertThat(deliberation1).isNotEqualTo(deliberation2);
    }
}
