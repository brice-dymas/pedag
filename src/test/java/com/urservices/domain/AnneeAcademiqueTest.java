package com.urservices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.urservices.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnneeAcademiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnneeAcademique.class);
        AnneeAcademique anneeAcademique1 = new AnneeAcademique();
        anneeAcademique1.setId(1L);
        AnneeAcademique anneeAcademique2 = new AnneeAcademique();
        anneeAcademique2.setId(anneeAcademique1.getId());
        assertThat(anneeAcademique1).isEqualTo(anneeAcademique2);
        anneeAcademique2.setId(2L);
        assertThat(anneeAcademique1).isNotEqualTo(anneeAcademique2);
        anneeAcademique1.setId(null);
        assertThat(anneeAcademique1).isNotEqualTo(anneeAcademique2);
    }
}
