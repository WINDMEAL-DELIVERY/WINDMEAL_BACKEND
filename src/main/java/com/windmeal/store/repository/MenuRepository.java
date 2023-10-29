package com.windmeal.store.repository;

import com.windmeal.store.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MenuRepository extends JpaRepository<Menu, Long> , MenuCustomRepository {


}
