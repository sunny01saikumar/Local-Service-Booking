package com.sai.geoLocation.mapper;

import com.sai.geoLocation.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default String displayName(User user) {
        return user == null ? null : (user.getFirstName() + " " + (user.getLastName() == null ? "" : user.getLastName())).trim();
    }
}
