package edu.sharif.math.yaadbuzz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserExtraMapperTest {

    private UserExtraMapper userExtraMapper;

    @BeforeEach
    public void setUp() {
        userExtraMapper = new UserExtraMapperImpl();
    }
}
