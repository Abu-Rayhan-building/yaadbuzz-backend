package edu.sharif.math.yaadbuzz.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserPerDepartmentMapperTest {

    private UserPerDepartmentMapper userPerDepartmentMapper;

    @BeforeEach
    public void setUp() {
        userPerDepartmentMapper = new UserPerDepartmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(userPerDepartmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userPerDepartmentMapper.fromId(null)).isNull();
    }
}
