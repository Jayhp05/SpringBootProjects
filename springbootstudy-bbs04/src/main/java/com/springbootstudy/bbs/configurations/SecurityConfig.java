package com.springbootstudy.bbs.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/* @Configuration은 스프링 환경설정 클래스를 지정하는 애노테이션으로
 * 이 애노테이션이 붙은 클래스는 스프링 DI 컨테이너가 초기화 될 때 빈으로 등록된다.
 **/
@Configuration
// 요청 URL이 스프링 시큐리티의 제어를 받도록 지정하는 애노테이션
@EnableWebSecurity
public class SecurityConfig {
	
	/* @Configuration이 적용된 클래스의 메서드에 @Bean 애노테이션을 지정하면
	 * 빈을 생성하는 메소드가 되며 이 메서드 안에서 반환하는 객체는 스프링 DI
	 * 컨테이너에 의해서 스프링 Bean으로 관리된다. 그리고 이 객체를 주입 받기를
	 * 원하는 @Autowired, @Inject 등과 같은 애노테이션이 적용된 곳으로 주입된다.
	 **/		
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/* @EnableWebSecurity 애너테이션을 사용하면 내부적으로 
	 * SpringSecurityFilterChain이 동작하여 URL 필터가 자동으로 적용된다.
	 **/		
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
			/* 스프링 시큐리티를 적용하면 모든 요청 URL에서 인증을 시도하여
			 * 로그인 창이 나타난다. 그래서 다음과 같이 별도로 인증하지 않도록
			 * 설정하면 사이트의 모든 페이지에 접근할 수 있다. 
			 **/
			authorizeHttpRequests -> 
				authorizeHttpRequests.requestMatchers(
						new AntPathRequestMatcher("/**"))
				.permitAll())
		
				/* 스프링 시큐리티가 CSRF 처리시 아래 URL은 예외로 처리하도록 설정 
				 * CSRF(cross site request forgery)는 웹 사이트 취약점 공격을 방지하기
				 * 위한 기술로 서버 상태를 변화시킬 수 있는 POST, PUT, PATCH, DELETE
				 * 등의 요청을 보호한다.
				 **/
				.csrf(csrf -> csrf.ignoringRequestMatchers(
						new AntPathRequestMatcher("/h2-console/**")))
				
				/* 스프링 시큐리티를 사용하면 CSRF protection(보호)이 기본 설정되기
				 * 때문에 초기 개발 중에는 일시적으로 CSRF 적용을 해제하기도 한다.
				 * 
				 * CSRF protection을 적용하였을 때, 서버의 상태를 변화시킬 수 있는
				 * POST 등의 요청을 서버로 보낼때는 CSRF 토큰이 포함되어야 요청을
				 * 받아들이게 된다. 만약 CSRF 토큰이 포함되지 않았으면 403(Forbidden)
				 * 오류가 발생한다. HTTP 403 상태 코드는 서버에 요청은 전달되었지만
				 * 서버에서 허용하지 않는 자원을 요청했기 때문에 거부하는 것으로
				 * 권한이 없는 자원에 접근할 때 해당 요청이 거절되었다는 의미이다.
				 * 
				 * 아래에서 CSRF를 disable 시키지 않으며 게시글 쓰기 등의 POST 요청에서
				 * CSRF 토큰이 포함되어야 요청을 받아들이기 때문에 게시글 쓰기 폼에는
				 * 다음과 같이 CSRF 토큰이 서버로 전달되도록 숨김 폼 컨트롤을 적용해야 한다. 
				 * 
				 * <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				 **/
				.csrf(csrf -> csrf.disable())
		
				// 스프링 시큐리티의 기본 로그인을 사용하지 않거나 로그인 페이지 지정하기
				// .formLogin(formLogin -> formLogin
				//		.disable()
				//		.loginPage("/login")
				//		.defaultSuccessUrl("/boardList"));
				
				// 스프링 시큐리티 로그아웃 기능 설정
				.logout((logout) -> logout
				//		.logoutUrl("/logout") 			// 기본 URL은 POST 방식의 /logout 
						.logoutSuccessUrl("/loginForm") // 로그아웃 성공 리다이렉트 페이지 
						.invalidateHttpSession(true));  // 기존 세션 삭제 여부 
		
		return http.build();		
	}
}
