package com.windmeal.global.token.dto;

import lombok.Getter;

@Getter
public class ReissueResponse {

  private final String token;

  private ReissueResponse(String token) {
    this.token = token;
  }

  public static ReissueResponse of (String token) {
    return new ReissueResponse(token);
  }

}
