package com.example.parking.repository.memory;

import com.example.parking.domain.model.ParkingSite;
import com.example.parking.repository.SiteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemorySiteRepository implements SiteRepository {
    private final ConcurrentMap<String, ParkingSite> stores = new ConcurrentHashMap<>();

    @Override
    public ParkingSite save(ParkingSite site) {
        stores.put(site.getSiteId(), site);
        return site;
    }

    @Override
    public Optional<ParkingSite> findById(String siteId) {
        return Optional.ofNullable(stores.get(siteId));
    }

    @Override
    public List<ParkingSite> findAll() {
        return new ArrayList<>(stores.values());
    }
}
