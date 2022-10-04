
package cn.zqyu.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * 路由配置信息
 *
 * @author Chill
 */
@Configuration
public class RouterFunctionConfiguration {

    /**
     * 这里为支持的请求头，如果有自定义的header字段请自己添加
     */
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_EXPOSE = "*";
    private static final Long MAX_AGE = 18000L;

    /**
     * 跨域解决办法之一：
     * 过滤器，给所有请求增加请求头信息
     * 使得预检请求通过
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1、配置跨域
        corsConfiguration.addAllowedHeader(ALLOWED_HEADERS);
        corsConfiguration.addAllowedMethod(ALLOWED_METHODS);
        // 当allowCredentials为真时，allowedorigin不能包含特殊值"*"，
        // 因为不能在"访问-控制-起源“响应头中设置该值。
        // 要允许凭证到一组起源，显示地列出它们，或者考虑使用"allowedOriginPatterns”代替。
        // corsConfiguration.addAllowedOrigin(ALLOWED_ORIGIN);
        corsConfiguration.addAllowedOriginPattern(ALLOWED_ORIGIN);
        // 否则跨域请求会丢失cookie信息
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Collections.singletonList(ALLOWED_EXPOSE));
        corsConfiguration.setMaxAge(MAX_AGE);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }

}
