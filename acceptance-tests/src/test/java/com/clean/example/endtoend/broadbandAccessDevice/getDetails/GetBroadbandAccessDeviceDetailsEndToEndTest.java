package com.clean.example.endtoend.broadbandAccessDevice.getDetails;

import com.clean.example.businessrequirements.broadbandAccessDevice.getDetails.GetBroadbandAccessDeviceDetailsAcceptanceTest;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.dataproviders.database.broadbandaccessdevice.BroadbandAccessDeviceDatabaseDataProvider;
import com.clean.example.dataproviders.database.exchange.ExchangeDatabaseDataProvider;
import com.clean.example.endtoend.EndToEndYatspecTest;
import com.clean.example.entrypoints.rest.broadbandaccessdevice.GetBroadbandAccessDeviceEndpoint;
import com.googlecode.yatspec.junit.LinkingNote;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import static com.cedarsoftware.util.io.JsonWriter.formatJson;
import static com.clean.example.core.entity.DeviceType.ADSL;
import static org.assertj.core.api.Assertions.assertThat;

@LinkingNote(message = "Business Requirements: %s", links = {GetBroadbandAccessDeviceDetailsAcceptanceTest.class})
public class GetBroadbandAccessDeviceDetailsEndToEndTest extends EndToEndYatspecTest {

    private static final String HOSTNAME = "device1.exchange1.com";
    private static final String SERIAL_NUMBER = "serialNumber1";
    private static final String EXCHANGE_CODE = "exchange1";
    private static final String EXCHANGE_NAME = "Exchange 1";
    private static final String EXCHANGE_POSTCODE = "A1 B23";
    private static final DeviceType DEVICE_TYPE = ADSL;

    @Autowired
    BroadbandAccessDeviceDatabaseDataProvider broadbandAccessDeviceDatabaseDataProvider;

    @Autowired
    ExchangeDatabaseDataProvider exchangeDatabaseDataProvider;

    int responseStatus;
    String responseContent;

    @Test
    public void returnsTheDetailsOfADevice() throws Exception {
        givenADeviceInOurModel();

        whenTheApiToGetTheDeviceDetailsIsCalledForThatDevice();

        thenTheDetailsOfTheDeviceAreReturned();
    }

    private void givenADeviceInOurModel() {
        exchangeDatabaseDataProvider.insert(new Exchange(EXCHANGE_CODE, EXCHANGE_NAME, EXCHANGE_POSTCODE));
        broadbandAccessDeviceDatabaseDataProvider.insert(EXCHANGE_CODE, HOSTNAME, SERIAL_NUMBER, DEVICE_TYPE);
        log("Device in model", "Exchange Code: " + EXCHANGE_CODE + "\nHostname: " + HOSTNAME + "\nSerial Number: " + SERIAL_NUMBER + "\nType: " + DEVICE_TYPE);
    }

    private void whenTheApiToGetTheDeviceDetailsIsCalledForThatDevice() throws UnirestException {
        String apiPath = GetBroadbandAccessDeviceEndpoint.API_PATH.replace("{hostname}", HOSTNAME);
        String apiUrl = "http://localhost:8080" + apiPath;
        log("API Url", apiUrl);

        HttpResponse<String> httpResponse = Unirest.get(apiUrl).asString();

        responseStatus = httpResponse.getStatus();
        log("Response Status", responseStatus);

        responseContent = httpResponse.getBody();
        log("Response Content", formatJson(responseContent));
    }

    private void thenTheDetailsOfTheDeviceAreReturned() {
        assertThat(responseStatus).isEqualTo(200);

        String expectedResponse =
                "{\n" +
                "  \"exchangeCode\":\"" + EXCHANGE_CODE + "\",\n" +
                "  \"hostname\":\"" + HOSTNAME + "\",\n" +
                "  \"serialNumber\":\"" + SERIAL_NUMBER + "\",\n" +
                "  \"type\":\"" + DEVICE_TYPE.name() + "\"\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse, responseContent, false);
    }

}
