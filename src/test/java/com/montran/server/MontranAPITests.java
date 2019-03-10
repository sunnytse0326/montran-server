package com.montran.server;

import com.montran.server.controller.CustomerController;
import com.montran.server.model.Customer;
import com.montran.server.repository.CustomerRepository;
import com.montran.server.security.MontranJWTService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MontranAPITests {
    private String testEmail = "test@test.com";
    private String testPassword = "123456";
    private String testName = "123456";
    private String testTransactionId = "testTransactionId";

    private String contentType = "application/json";

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerRepository customerRepository;

    private String userUrl = "/user";
    private String registerUrl = "/register";
    private String loginUrl = "/login";
    private String profileUrl = "/me";
    private String transactionUrl = "/transaction";

    private MockMvc mockMvc;
    private Customer customer;
    private MontranJWTService service;

    @Before
    public void initData() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        service = MontranJWTService.getInstance();

        customer = new Customer();
        customer.setEmail(testEmail);
        customer.setPassword(service.hashPassword(testPassword));
        given(customerRepository.findCustomerByEmail(testEmail)).willReturn(customer);
    }

    @Test
    public void registerAccountTest() throws Exception {
        this.mockMvc.perform(post(userUrl + registerUrl)).andExpect(status().is4xxClientError());
        this.mockMvc.perform(post(userUrl + registerUrl).contentType(contentType).content(registerJson())).andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post(userUrl + registerUrl).contentType(contentType).content(registerJson())).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    public void loginAccountTest() throws Exception {
        customer = new Customer(testName, testName, testEmail, MontranJWTService.getInstance().hashPassword(testPassword));
        customerRepository.save(customer);

        this.mockMvc.perform(post(userUrl + loginUrl)).andExpect(status().is4xxClientError());
        this.mockMvc.perform(post(userUrl + loginUrl).contentType(contentType).content(loginJson())).andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post(userUrl + loginUrl).contentType(contentType).content(loginJson())).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    public void getProfile() throws Exception {
        this.mockMvc.perform(get(userUrl + profileUrl)).andExpect(status().is4xxClientError());
        this.mockMvc.perform(get(userUrl + profileUrl).contentType(contentType).header("Authorization", "")).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
        String token = service.generatorJWT(testEmail);
        this.mockMvc.perform(get(userUrl + profileUrl).contentType(contentType).header("Authorization", "Bearer " + token)).andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    public void makeTransaction() throws Exception {
        this.mockMvc.perform(post(userUrl + transactionUrl)).andExpect(status().is4xxClientError());
        this.mockMvc.perform(post(userUrl + transactionUrl).contentType(contentType).content(transactionJson()).header("Authorization", "")).andExpect(status().is2xxSuccessful());
        String token = service.generatorJWT(testEmail);
        this.mockMvc.perform(post(userUrl + transactionUrl).contentType(contentType).content(transactionJson()).header("Authorization", "Bearer " + token)).andExpect(status().is2xxSuccessful());
    }


    private String registerJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("email", testEmail);
        obj.put("password", testPassword);
        obj.put("firstName", testName);
        obj.put("lastName", testName);
        return obj.toString();
    }

    private String loginJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("email", testEmail);
        obj.put("password", testPassword);
        return obj.toString();
    }

    private String transactionJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("transactionId", testTransactionId);
        return obj.toString();
    }

}
