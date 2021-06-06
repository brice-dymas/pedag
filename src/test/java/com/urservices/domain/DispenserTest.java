package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DispenserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispenser.class);
        Dispenser dispenser1 = new Dispenser();
        dispenser1.setId(1L);
        Dispenser dispenser2 = new Dispenser();
        dispenser2.setId(dispenser1.getId());
        assertThat(dispenser1).isEqualTo(dispenser2);
        dispenser2.setId(2L);
        assertThat(dispenser1).isNotEqualTo(dispenser2);
        dispenser1.setId(null);
        assertThat(dispenser1).isNotEqualTo(dispenser2);
    }
}
