package com.hackzurich.hackzurichteam12.backend.model.repo;

import com.hackzurich.hackzurichteam12.backend.model.entity.LocationEntity;
import com.hackzurich.hackzurichteam12.backend.model.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<LocationEntity, UUID> {
    boolean existsByLongitudeAndLatitude(double longitude, double latitude);
    LocationEntity findFirstByLongitudeAndLatitude(double longitude, double latitude);
    List<LocationEntity> findAllByLongitudeBetweenAndLatitudeBetween(double longitudeMin, double longitudeMax, double latitudeMin, double latitudeMax);
}
