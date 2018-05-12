package com.clean.example.endtoend;

import com.clean.example.yatspec.SpringSpecRunner;
import com.clean.example.yatspec.YatspecTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringSpecRunner.class)
@SpringBootTest(classes = {ApplicationConfigurationForEndToEndTests.class, MocksConfigurationForEndToEndTests.class})
@WebAppConfiguration
public abstract class EndToEndYatspecTest extends YatspecTest {

}
