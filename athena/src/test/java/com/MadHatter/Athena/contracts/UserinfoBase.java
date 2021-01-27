package com.MadHatter.Athena.contracts;


import com.MadHatter.Athena.helpers.UserInfoFactory;
import com.MadHatter.Athena.models.AthenaUserInfo;
import com.MadHatter.Athena.models.UserInfo;
import com.MadHatter.Athena.commands.GetUserInfoCommand;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public abstract class UserinfoBase {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private GetUserInfoCommand getUserInfoCommand;

    @Before
    public void setUp() {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .dispatchOptions(true)
                .build();
        AthenaUserInfo userInfo = new AthenaUserInfo("0dccc8e2-ac1c-4031-9008-7d745f58b3a2",
                "FUser",
                "Fake MadHatterUser unknown.org",
                "FUser",
                "unknown.org",
                "FUser@unknown.org",
                false,
                1556051070104L,
                "0dccc8e2-ac1c-4031-9008-7d745f58b3a2",
                "BOLT"
                );
        Mockito.when(getUserInfoCommand.execute(any(String.class))).thenReturn( new ResponseEntity(userInfo, HttpStatus.OK));
        Mockito.when(getUserInfoCommand.execute(null)).thenReturn( new ResponseEntity( HttpStatus.BAD_REQUEST));

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
