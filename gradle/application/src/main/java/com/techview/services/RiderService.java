package com.techview.services;

import com.techview.model.Rider;
import com.techview.model.RiderType;

import java.util.List;

public interface RiderService {
    List<Rider> findBy(RiderType riderType);
}
