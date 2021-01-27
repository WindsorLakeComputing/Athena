package com.MadHatter.Athena.controllers.security

import org.springframework.security.test.context.support.WithSecurityContext
import kotlin.annotation.Retention


@Retention
@WithSecurityContext(factory = WithMockOAuth2ScopeSecurityContextFactory::class)
annotation class WithMockOAuth2Scope(val scopes: Array<String> = [])


