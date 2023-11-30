package com.windmeal.member.repository;

import com.windmeal.member.dto.response.BlackListResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BlackListCustomRepository {
  Page<BlackListResponse> getBlackListByRequesterId(Pageable pageable, Long requesterId);
}
