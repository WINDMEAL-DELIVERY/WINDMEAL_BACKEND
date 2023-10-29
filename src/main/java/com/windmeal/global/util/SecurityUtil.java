package com.windmeal.global.util;

import com.windmeal.global.exception.ErrorCode;
import com.windmeal.member.exception.NotAuthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static com.windmeal.global.constants.SecurityConstants.ANONYMOUSUSER;

public class SecurityUtil {

    /**
     * 접속한 사용자의 정보를 반환해주는 유틸 메소드
     * @return 인증된 사용자라면 이메일을, 그렇지 않다면 null 반환
     */
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authenticationCheck(authentication);
        return Long.parseLong(authentication.getName());
    }

    /**
     * 접속한 사용자의 정보를 반환해주는 유틸 메소드
     * @return 인증된 사용자라면 이메일을, 그렇지 않다면 null 반환
     */
    public static Long getCurrentNullableMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null ||
                authentication.getName().equals(ANONYMOUSUSER)) {
            return null;
        }
        return Long.parseLong(authentication.getName());
    }

    /**
     * 접속한 사용자의 정보를 반환해주는 유틸 메소드
     * @return 인증된 사용자라면 이메일을, 그렇지 않다면 null 반환
     */
    public static String getCurrentMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authenticationCheck(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return principal.getPassword();
    }

    /**
     * 접속한 사용자의 정보를 반환해주는 유틸 메소드
     * @return 인증된 사용자라면 이메일을, 그렇지 않다면 null 반환
     */
    public static String getCurrentNullableMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null ||
                authentication.getName().equals(ANONYMOUSUSER)) {
            return null;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getPassword();
    }

    private static void authenticationCheck(Authentication authentication) {
        if(authentication == null || authentication.getName() == null ||
                authentication.getName().equals(ANONYMOUSUSER)) {
            throw new NotAuthorizedException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }
    }
}
