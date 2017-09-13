package com.clean.example.businessrequirements.broadbandAccessDevice.getDetails;

import com.clean.example.core.entity.BroadbandAccessDevice;
import com.clean.example.core.entity.DeviceType;
import com.clean.example.core.entity.Exchange;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.DeviceNotFoundException;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetBroadbandAccessDeviceDetailsUseCase;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetDeviceDetails;
import com.clean.example.endtoend.broadbandAccessDevice.getDetails.GetBroadbandAccessDeviceDetailsEndToEndTest;
import com.clean.example.yatspec.YatspecTest;
import com.googlecode.yatspec.junit.LinkingNote;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@LinkingNote(message = "End to End test: %s", links = {GetBroadbandAccessDeviceDetailsEndToEndTest.class})
public class GetBroadbandAccessDeviceDetailsAcceptanceTest extends YatspecTest {

    private static final String EXCHANGE_CODE = "exch1";
    private static final String HOSTNAME = "device1.exch1.com";
    private static final String SERIAL_NUMBER = "serialNumber1";
    private static final DeviceType DEVICE_TYPE = DeviceType.ADSL;

    GetDeviceDetails getDeviceDetails = mock(GetDeviceDetails.class);

    GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase = new GetBroadbandAccessDeviceDetailsUseCase(getDeviceDetails);

    BroadbandAccessDevice deviceDetails;
    DeviceNotFoundException deviceNotFoundException;

    @Test
    public void returnsTheDetailsOfTheDevice() throws Exception {
        givenADeviceInOurModel();

        whenTheApiToGetTheDeviceDetailsIsCalledForThatDevice();

        thenTheDetailsOfTheDeviceAreReturned();
    }

    @Test
    public void errorWhenDeviceIsNotFound() throws Exception {
        givenTheDeviceDoesNotExist();

        whenTheApiToGetTheDeviceDetailsIsCalledForThatDevice();

        thenAnErrorIsReturned();
    }

    private void givenADeviceInOurModel() {
        BroadbandAccessDevice device = new BroadbandAccessDevice(HOSTNAME, SERIAL_NUMBER, DEVICE_TYPE);
        device.setExchange(new Exchange(EXCHANGE_CODE, "exchangeName", "exchangePostcode"));
        when(getDeviceDetails.getDetails(HOSTNAME)).thenReturn(device);
        log("Device in model", device);
    }

    private void givenTheDeviceDoesNotExist() {
        when(getDeviceDetails.getDetails(HOSTNAME)).thenReturn(null);
    }

    private void whenTheApiToGetTheDeviceDetailsIsCalledForThatDevice() {
        try {
            deviceDetails = getBroadbandAccessDeviceDetailsUseCase.getDeviceDetails(HOSTNAME);
            log("Device details returned", deviceDetails);
        } catch (DeviceNotFoundException e) {
            this.deviceNotFoundException = e;
            log("Error received", deviceNotFoundException);
        }
    }

    private void thenTheDetailsOfTheDeviceAreReturned() {
        assertThat(deviceDetails.getHostname()).isEqualTo(HOSTNAME);
        assertThat(deviceDetails.getSerialNumber()).isEqualTo(SERIAL_NUMBER);
        assertThat(deviceDetails.getType()).isEqualTo(DEVICE_TYPE);
        assertThat(deviceDetails.getExchange().getCode()).isEqualTo(EXCHANGE_CODE);
    }

    private void thenAnErrorIsReturned() {
        assertThat(deviceNotFoundException).isNotNull();
    }

}
