package com.windmeal.model.place;

import com.windmeal.store.domain.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long>{

  Optional<Place> findByNameAndLongitudeAndLatitude(String name,Double longitude, Double latitude);
}
