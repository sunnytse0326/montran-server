package com.montran.server;

import com.montran.server.security.MontranJWTService;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MontranSecurityTests {
    private MontranJWTService service;
    private String testEmail = "test@test.com";
    private String testPassword = "123456";

    @Before
    public void initService(){
        service = MontranJWTService.getInstance();
    }

    @Test
    public void jwtTest() {
        String jwtToken = MontranJWTService.getInstance().generatorJWT(testEmail);
        assertTrue(MontranJWTService.getInstance().verifyJWT(jwtToken));

        Claims claims = MontranJWTService.getInstance().getJWTClaimerInfo(jwtToken);
        assertEquals(claims.get(service.getEmailTag()), testEmail);

        assertEquals("email", service.getEmailTag());
    }

    @Test
    public void hashPasswordTest(){
        assertTrue(service.verifyPassword(testPassword, service.hashPassword(testPassword)));
        assertNotEquals(true, service.verifyPassword("678890", service.hashPassword(testPassword)));
    }

}
