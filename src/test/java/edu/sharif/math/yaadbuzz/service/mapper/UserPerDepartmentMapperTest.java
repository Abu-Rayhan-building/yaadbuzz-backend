package edu.sharif.math.yaadbuzz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPerDepartmentMapperTest {

    private UserPerDepartmentMapper userPerDepartmentMapper;

    @BeforeEach
    public void setUp() {
        userPerDepartmentMapper = new UserPerDepartmentMapperImpl();
    }
}
