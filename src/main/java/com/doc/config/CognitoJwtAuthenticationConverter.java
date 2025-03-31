package com.doc.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class CognitoJwtAuthenticationConverter
    implements Converter<Jwt, AbstractAuthenticationToken> {

  @Override
  public AbstractAuthenticationToken convert(Jwt source) {
    Collection<GrantedAuthority> authorities = extractAuthorities(source);
    return new JwtAuthenticationToken(source, authorities);
  }

  private Collection<GrantedAuthority> extractAuthorities(Jwt source) {
    List<String> groups = source.getClaimAsStringList("cognito:groups");
    return groups != null
        ? groups.stream()
            .map(group -> new SimpleGrantedAuthority("ROLE_" + group.toUpperCase()))
            .collect(Collectors.toList())
        : Collections.emptyList();
  }
}
