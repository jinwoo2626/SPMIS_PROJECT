package dongyang.spmis.config;

import dongyang.spmis.oauth.CustomOauth2UserService;
import dongyang.spmis.security.CustomLoginSuccessHandler;
import dongyang.spmis.security.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//https://www.codejava.net/frameworks/spring-boot/fix-websecurityconfigureradapter-deprecated

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Autowired
//    private CorsConfig corsConfig;
//
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    public SecurityConfig(
//            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
//            JwtAccessDeniedHandler jwtAccessDeniedHandler){
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
//    }

    @Autowired
    CustomLoginSuccessHandler customLoginSuccessHandler;

    @Autowired
    CustomOauth2UserService customOauth2UserService;

    @Autowired
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;


    // ????????? ????????? ???????????? ????????? configure ??? override ?????? ????????? ?????? ?????? ???????????? ???????????? ????????????.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**"
                , "/favicon.ico"
                , "/error", "/api/test",
                "/assets/**","/join",
                "/emailcheck","/emailcheck2", "/sendEmail", "/publicprojectlist",
                "/checknumconfirm", "/register", "/passwordfind",
                "/passwordfind2", "/passwordfindPost","/passwordchange","/passwordchangefin"
                );
    }
    // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                        .antMatchers("/login").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/signupPOST").permitAll() // ????????????
                    .antMatchers("/login/**").permitAll() // ????????????
                    .anyRequest()
                        .access("hasRole('ROLE_USER')")
//                .anyRequest().authenticated()

                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .usernameParameter("user_email")
                    .passwordParameter("user_pw")
                    .loginProcessingUrl("/login")
                .successHandler(customLoginSuccessHandler)
                .and()
                    .oauth2Login()
                    .loginPage("/login")
                .redirectionEndpoint()
//                .baseUri("login/oauth2/**")
                .and()
                    // ?????? ???????????? ????????? ????????? ???????????? ?????????. 1. ????????????(??????), 2.????????? ??????(??????) 3. ????????? ????????? ?????? ????????????
                    // 4-1. ??? ????????? ????????? ??????????????? ???????????? ??????
                    // 4-2. (?????????, ????????????, ??????, ?????????) ????????? -> ????????? ????????? ???
                    // ????????? ????????? ????????? ????????? ????????? ?????????
                    .userInfoEndpoint()
                    .userService(customOauth2UserService)

                        .and()
                .successHandler(oAuth2AuthenticationSuccessHandler);
        return httpSecurity.build();

    }


    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
    authorizationRequestRepository() {

        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

}
