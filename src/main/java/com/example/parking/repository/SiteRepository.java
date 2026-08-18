package com.example.parking.repository;

import com.example.parking.domain.model.ParkingSite;

import java.util.Optional;

public interface SiteRepository {
    ParkingSite save(ParkingSite site);

    Optional<ParkingSite> findById(String siteId);

    java.util.List<ParkingSite> findAll();
}
