package com.clean.example.integration.rest.broadbandaccessdevice.getdetails;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.DeviceNotFoundException;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetBroadbandAccessDeviceDetailsUseCase;
import com.clean.example.entrypoints.rest.broadbandaccessdevice.GetBroadbandAccessDeviceEndpoint;
import com.clean.example.yatspec.YatspecTest;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.cedarsoftware.util.io.JsonWriter.formatJson;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.json.JSONException;

public class GetBroadbandAccessDeviceRestIntegrationTests extends YatspecTest {

    private static final String EXCHANGE_CODE = "exchange";
    private static final String HOSTNAME = "device1.exchange.com";
    private static final String SERIAL_NUMBER = "serialNumber1";
    private static final DeviceType DEVICE_TYPE = DeviceType.ADSL;

    GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase = mock(GetBroadbandAccessDeviceDetailsUseCase.class);

    private MockMvc mockMvc;
    private String responseContent;
    private int responseStatusCode;

    @Before
    public void setUp() throws Exception {
        initialiseEndpoint();
    }

    @Test
    public void deviceDetailsAreReturnedAsJson() throws Exception {
        givenADeviceExists();

        whenTheGetDetailsApiIsCalled();

        thenTheDetailsAreReturned();
    }

    @Test
    public void returns404WhenDeviceIsNotFound() throws Exception {
        givenADeviceDoesNotExist();

        whenTheGetDetailsApiIsCalled();

        thenItReturns404();
    }

    private void initialiseEndpoint() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GetBroadbandAccessDeviceEndpoint(getBroadbandAccessDeviceDetailsUseCase)).build();
    }

    private void givenADeviceExists() {
        BroadbandAccessDevice device = new BroadbandAccessDevice(HOSTNAME, SERIAL_NUMBER, DEVICE_TYPE);
        device.setExchange(new Exchange(EXCHANGE_CODE, "exchangeName", "exchangePostcode"));
        when(getBroadbandAccessDeviceDetailsUseCase.getDeviceDetails(HOSTNAME)).thenReturn(device);
        log("Device in model", device);
    }

    private void givenADeviceDoesNotExist() {
        when(getBroadbandAccessDeviceDetailsUseCase.getDeviceDetails(HOSTNAME)).thenThrow(new DeviceNotFoundException());
    }

    private void whenTheGetDetailsApiIsCalled() throws Exception {
        log("Request Path", GetBroadbandAccessDeviceEndpoint.API_PATH);
        MvcResult mvcResult = mockMvc.perform(get(GetBroadbandAccessDeviceEndpoint.API_PATH, HOSTNAME)).andReturn();
        responseContent = mvcResult.getResponse().getContentAsString();
        responseStatusCode = mvcResult.getResponse().getStatus();
        log("Response: Status Code", responseStatusCode);
    }

    private void thenTheDetailsAreReturned() throws JSONException {
        log("Response: Content", formatJson(responseContent));
        assertThat(responseStatusCode).isEqualTo(200);

        String expectedResponse =
                "{\n" +
                "  \"exchangeCode\":\"exchange\",\n" +
                "  \"hostname\":\"device1.exchange.com\",\n" +
                "  \"serialNumber\":\"serialNumber1\",\n" +
                "  \"type\":\"ADSL\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }

    private void thenItReturns404() {
        assertThat(responseStatusCode).isEqualTo(404);
    }

}
