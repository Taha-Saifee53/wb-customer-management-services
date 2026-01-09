package com.wb.assignment.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RabbitMQMessageConfig.class)
class RabbitMQMessageConfigTest {

    @Autowired
    private JacksonJsonMessageConverter jacksonJsonMessageConverter;

    @Test
    void shouldCreateJacksonJsonMessageConverterBean() {
        assertThat(jacksonJsonMessageConverter).isNotNull();
        assertThat(jacksonJsonMessageConverter)
                .isInstanceOf(JacksonJsonMessageConverter.class);
    }
}
