package com.windmeal.store.validator;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class StoreValidator {

  public boolean validateStoreIsOpen(LocalTime openTime, LocalTime closeTime, LocalTime now) {

    if (openTime == null || closeTime == null) {
      return true;
    }

    if (now.isBefore(openTime) || now.isAfter(closeTime)) {
      return false;
    } else {
      return true;
    }

  }
}
