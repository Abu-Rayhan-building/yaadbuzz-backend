package edu.sharif.math.yaadmaan.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.sharif.math.yaadmaan.service.mapper.DepartmentMapper;
import edu.sharif.math.yaadmaan.service.mapper.DepartmentMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartmentMapperTest {

    private DepartmentMapper departmentMapper;

    @BeforeEach
    public void setUp() {
        departmentMapper = new DepartmentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(departmentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(departmentMapper.fromId(null)).isNull();
    }
}
