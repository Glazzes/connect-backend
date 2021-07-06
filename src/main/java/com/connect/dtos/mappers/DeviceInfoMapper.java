package com.connect.dtos.mappers;

import com.connect.dtos.DeviceInfoDto;
import com.connect.entities.embedables.DeviceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceInfoMapper {

    DeviceInfoMapper INSTANCE = Mappers.getMapper(DeviceInfoMapper.class);

    @Mapping(source = "type", target = "type")
    @Mapping(source = "deviceOperatingSystem", target = "deviceOperatingSystem")
    @Mapping(source = "deviceName", target = "deviceName")
    @Mapping(source = "deviceVersion", target = "deviceVersion")
    DeviceInfo deviceInfoDtoToDeviceInfo(DeviceInfoDto deviceInfoDto);

}
