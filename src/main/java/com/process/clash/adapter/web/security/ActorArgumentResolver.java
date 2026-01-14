package com.process.clash.adapter.web.security;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.infrastructure.principle.AuthUser;
import java.nio.file.AccessDeniedException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ActorArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedActor.class)
            && Actor.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated request");
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new AccessDeniedException("Missing principal");
        }

        // ✅ 1) principal 자체가 Actor인 경우 (가장 이상적)
        if (principal instanceof Actor actor) {
            return actor;
        }

        // ✅ 2) 기존 방식 호환: principal이 AuthUser면 Actor로 변환해서 주입
        if (principal instanceof AuthUser authUser) {
            return authUser.toActor();
        }

        // ✅ 3) 기타 타입이면 명확히 막기
        throw new AccessDeniedException("Unsupported principal type: " + principal.getClass().getName());
    }
}