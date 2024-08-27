package com.dev.hyper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class HyperApplicationTests {

    @Test
    void contextLoads() {
    }

}
