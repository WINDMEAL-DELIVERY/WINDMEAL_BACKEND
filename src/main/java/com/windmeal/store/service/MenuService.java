package com.windmeal.store.service;


import com.windmeal.store.repository.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

  private final MenuJpaRepository menuRepository;

  @Transactional
  public void createMenu(){

  }
}
