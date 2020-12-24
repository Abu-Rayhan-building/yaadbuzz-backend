package edu.sharif.math.yaadbuzz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemorialMapperTest {

    private MemorialMapper memorialMapper;

    @BeforeEach
    public void setUp() {
        memorialMapper = new MemorialMapperImpl();
    }
}
