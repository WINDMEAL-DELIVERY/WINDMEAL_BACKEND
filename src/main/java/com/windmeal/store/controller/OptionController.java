package com.windmeal.store.controller;


import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.OptionCreateRequest;
import com.windmeal.store.service.OptionService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OptionController {

  private final OptionService optionService;

  @PostMapping("/menu/{menuId}/option")
  public ResultDataResponseDTO createOption(
      @Valid @RequestBody OptionCreateRequest request,
      @PathVariable Long menuId){
    optionService.createOption(request,menuId);
    return ResultDataResponseDTO.empty();
  }
}
