package com.windmeal.member.controller;

import com.windmeal.global.exception.ExceptionResponseDTO;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.global.util.SecurityUtil;
import com.windmeal.member.dto.request.BlackListCreateRequest;
import com.windmeal.member.dto.response.BlackListResponse;
import com.windmeal.member.service.BlackListService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "차단", description = "사용자 차단 관련 API 입니다.")
@RequestMapping(value = "/api/member/blackList")
public class BlackListController {

    private final BlackListService blackListService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 차단 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "사용자(요청자 혹은 대상자)를 찾을 수 없습니다.",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
    })
    @PostMapping
    public ResultDataResponseDTO addBlackList(@RequestBody BlackListCreateRequest blackListCreateRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        blackListService.addBlackList(currentMemberId, blackListCreateRequest);
        return ResultDataResponseDTO.empty();
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 차단목록 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "사용자(요청자)를 찾을 수 없습니다.",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
    })
    @GetMapping
    public ResultDataResponseDTO<Page<BlackListResponse>> getBlackList(Pageable pageable) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResultDataResponseDTO.of(blackListService.getBlackList(currentMemberId, pageable));
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "사용자 차단목록 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "사용자(요청자)를 찾을 수 없습니다.",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDTO.class)))
    })
    @DeleteMapping
    public ResultDataResponseDTO deleteBlackList(@RequestParam(name = "blackListId") Long blackListId) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        blackListService.deleteBlackList(currentMemberId, blackListId);
        return ResultDataResponseDTO.empty();
    }

}
