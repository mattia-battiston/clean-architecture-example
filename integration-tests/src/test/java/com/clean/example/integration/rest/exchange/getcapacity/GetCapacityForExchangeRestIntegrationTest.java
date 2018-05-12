package com.clean.example.integration.rest.exchange.getcapacity;

import com.clean.example.core.entity.Capacity;
import com.clean.example.core.usecase.exchange.getcapacity.ExchangeNotFoundException;
import com.clean.example.core.usecase.exchange.getcapacity.GetCapacityForExchangeUseCase;
import com.clean.example.entrypoints.rest.exchange.capacity.GetCapacityForExchangeEndpoint;
import com.clean.example.yatspec.YatspecTest;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.cedarsoftware.util.io.JsonWriter.formatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.json.JSONException;

public class GetCapacityForExchangeRestIntegrationTest extends YatspecTest {

    private static final String EXCHANGE_CODE = "exch1";

    GetCapacityForExchangeUseCase getCapacityForExchangeUseCase = mock(GetCapacityForExchangeUseCase.class);

    MockMvc mockMvc;
    String responseContent;
    int responseStatusCode;

    @Before
    public void setUp() throws Exception {
        initialiseEndpoint();
    }

    @Test
    public void returnsCapacityForAnExchange() throws Exception {
        givenThereIsCapacityForTheExchange();

        whenTheCapacityIsRetrieved();

        thenTheCapacityIsReturned();
    }

    @Test
    public void returns404WhenTheExchangeIsNotFound() throws Exception {
        givenTheExchangeDoesNotExist();

        whenTheCapacityIsRetrieved();

        thenItReturns404();
    }

    private void initialiseEndpoint() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GetCapacityForExchangeEndpoint(getCapacityForExchangeUseCase)).build();
    }

    private void givenThereIsCapacityForTheExchange() {
        Capacity capacity = new Capacity(true, false);
        when(getCapacityForExchangeUseCase.getCapacity(EXCHANGE_CODE)).thenReturn(capacity);
        log("Capacity in exchange " + EXCHANGE_CODE, capacity);
    }

    private void givenTheExchangeDoesNotExist() {
        when(getCapacityForExchangeUseCase.getCapacity(EXCHANGE_CODE)).thenThrow(new ExchangeNotFoundException());
    }

    private void whenTheCapacityIsRetrieved() throws Exception {
        log("Request Path", GetCapacityForExchangeEndpoint.API_PATH);
        MvcResult mvcResult = mockMvc.perform(get(GetCapacityForExchangeEndpoint.API_PATH, EXCHANGE_CODE)).andReturn();
        responseContent = mvcResult.getResponse().getContentAsString();
        responseStatusCode = mvcResult.getResponse().getStatus();
        log("Response: Status Code", responseStatusCode);
    }

    private void thenTheCapacityIsReturned() throws JSONException {
        log("Response: Content", formatJson(responseContent));
        assertThat(responseStatusCode).isEqualTo(200);

        String expectedResponse =
                "{\n" +
                "  \"hasADSLCapacity\":true,\n" +
                "  \"hasFibreCapacity\":false\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }

    private void thenItReturns404() {
        assertThat(responseStatusCode).isEqualTo(404);
    }

}
