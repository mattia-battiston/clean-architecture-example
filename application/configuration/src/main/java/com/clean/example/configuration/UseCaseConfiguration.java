package com.clean.example.configuration;

import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetBroadbandAccessDeviceDetailsUseCase;
import com.clean.example.core.usecase.broadbandaccessdevice.getdetails.GetDeviceDetails;
import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.*;
import com.clean.example.core.usecase.exchange.getcapacity.DoesExchangeExist;
import com.clean.example.core.usecase.exchange.getcapacity.GetAvailablePortsOfAllDevicesInExchange;
import com.clean.example.core.usecase.exchange.getcapacity.GetCapacityForExchangeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase(GetAllDeviceHostnames getDeviceDetailsFromMode, GetSerialNumberFromModel getSerialNumberFromModel, GetSerialNumberFromReality getSerialNumberFromReality, UpdateSerialNumberInModel updateSerialNumberInModel) {
        return new ReconcileBroadbandAccessDevicesUseCase(getDeviceDetailsFromMode, getSerialNumberFromModel, getSerialNumberFromReality, updateSerialNumberInModel);
    }

    @Bean
    public GetBroadbandAccessDeviceDetailsUseCase getBroadbandAccessDeviceDetailsUseCase(GetDeviceDetails getDeviceDetails) {
        return new GetBroadbandAccessDeviceDetailsUseCase(getDeviceDetails);
    }

    @Bean
    public GetCapacityForExchangeUseCase getCapacityForExchangeUseCase(DoesExchangeExist doesExchangeExist, GetAvailablePortsOfAllDevicesInExchange getAvailablePortsOfAllDevicesInExchange) {
        return new GetCapacityForExchangeUseCase(doesExchangeExist, getAvailablePortsOfAllDevicesInExchange);
    }

}
