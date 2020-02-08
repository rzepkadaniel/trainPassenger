package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class TrainTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Train.class);
        Train train1 = new Train();
        train1.setId(1L);
        Train train2 = new Train();
        train2.setId(train1.getId());
        assertThat(train1).isEqualTo(train2);
        train2.setId(2L);
        assertThat(train1).isNotEqualTo(train2);
        train1.setId(null);
        assertThat(train1).isNotEqualTo(train2);
    }
}
