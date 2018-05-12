package com.clean.example.endtoend;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.clean.example.yatspec.SpringSpecRunner;
import com.clean.example.yatspec.YatspecTest;

@RunWith(SpringSpecRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT, classes = {ApplicationConfigurationForEndToEndTests.class, MocksConfigurationForEndToEndTests.class})
public abstract class EndToEndYatspecTest extends YatspecTest {

}
