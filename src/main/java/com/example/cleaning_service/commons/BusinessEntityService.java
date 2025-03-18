package com.example.cleaning_service.commons;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class BusinessEntityService {

    @Transactional
    public void updateBusinessEntityFields(BusinessEntity entity, @NotNull BusinessEntityRequest updateRequest) {
        if (updateRequest.name() != null) entity.setName(updateRequest.name());
        if (updateRequest.address() != null) entity.setAddress(updateRequest.address());
        if (updateRequest.phone() != null) entity.setPhone(updateRequest.phone());
        if (updateRequest.email() != null) entity.setEmail(updateRequest.email());
        if (updateRequest.city() != null) entity.setCity(updateRequest.city());
        if (updateRequest.state() != null) entity.setState(updateRequest.state());
        if (updateRequest.zip() != null) entity.setZip(updateRequest.zip());
        if (updateRequest.country() != null) entity.setCountry(updateRequest.country());
        if (updateRequest.notes() != null) entity.setNotes(updateRequest.notes());
    }
}
