package com.connect.session.application.mapper;

import com.connect.session.domain.model.DeviceInfoModel;
import com.connect.session.domain.entities.embedable.DeviceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceInfoMapper {

    DeviceInfoMapper INSTANCE = Mappers.getMapper(DeviceInfoMapper.class);

    @Mapping(source = "type", target = "type")
    @Mapping(source = "appDetails", target = "appDetails")
    @Mapping(source = "deviceDetails", target = "deviceDetails")
    @Mapping(source = "ipAddress", target = "ipAddress")
    DeviceInfo deviceInfoModelToDeviceInfo(DeviceInfoModel deviceInfoModel);

}
