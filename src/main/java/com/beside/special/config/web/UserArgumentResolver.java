package com.beside.special.config.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.beside.special.domain.AuthUser;
import com.beside.special.domain.dto.UserDto;
import com.beside.special.exception.AuthorizationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class) &&
            parameter.getParameterType().equals(UserDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("authorization");
        if (authorization == null) {
            throw new AuthorizationException("인증정보가 없습니다!");
        }

        String token = authorization.split(" ")[1];

        if (token == null) {
            throw new AuthorizationException("인증정보가 없습니다!");
        }

        DecodedJWT jwt = JWT.require(Algorithm.HMAC256("qwelrkjasdlfvkjasd")).build()
            .verify(token);

        return new UserDto(
            jwt.getClaim("provider").asString(),
            jwt.getClaim("userId").asString(),
            jwt.getClaim("email").asString(),
            "a"
        );
    }
}
