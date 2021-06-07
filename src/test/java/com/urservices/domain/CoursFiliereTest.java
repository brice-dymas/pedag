package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoursFiliereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoursFiliere.class);
        CoursFiliere coursFiliere1 = new CoursFiliere();
        coursFiliere1.setId(1L);
        CoursFiliere coursFiliere2 = new CoursFiliere();
        coursFiliere2.setId(coursFiliere1.getId());
        assertThat(coursFiliere1).isEqualTo(coursFiliere2);
        coursFiliere2.setId(2L);
        assertThat(coursFiliere1).isNotEqualTo(coursFiliere2);
        coursFiliere1.setId(null);
        assertThat(coursFiliere1).isNotEqualTo(coursFiliere2);
    }
}
