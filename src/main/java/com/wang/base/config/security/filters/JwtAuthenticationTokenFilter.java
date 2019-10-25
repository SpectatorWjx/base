package com.wang.base.config.security.filters;

import com.alibaba.fastjson.JSON;
import com.wang.base.common.utils.*;
import com.wang.base.config.SecurityUserService;
import com.wang.base.common.utils.RedisUtil;
import com.wang.base.common.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wjx
 * @date: 2018/10/15 17:30
 * @description: 确保在一次请求只通过一次filter，而不需要重复执行
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    SecurityUserService userDetailsService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json; charset=UTF-8");

        String authHeader = request.getHeader("Authorization");

        //获取请求的ip地址
        String currentIp = IpUtil.getIpAddr(request);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring("Bearer ".length());

            String username = JwtTokenUtil.parseToken(authToken, "salt");
            String clientId = CollectionUtil.getMapValue(JwtTokenUtil.getClaims(authToken), "clientId");

            /*
            进入黑名单验证
             */
            if (redisUtil.isBlackList(authToken)) {
                log.info("用户：{}的token：{}在黑名单之中，拒绝访问",username,authToken);
                response.getWriter().println(JSON.toJSONString(ResultUtil.exception(ResultEnum.TOKEN_IS_BLACKLIST.getCode(),ResultEnum.TOKEN_IS_BLACKLIST.getMessage())));
                return;
            }
            /*
             * 判断token是否过期
             * 过期的话，从redis中读取有效时间（比如七天登录有效），再refreshToken（根据以后业务加入，现在直接refresh）
             * 同时，已过期的token加入黑名单
             */
            if (redisUtil.hasToken(authToken)) {
                String expirationTime = redisUtil.getExpirationTimeByToken(authToken).toString();
                if (JwtTokenUtil.isExpiration(expirationTime)) {//获得redis中用户的token刷新时效
                    String tokenValidTime = (String) redisUtil.getTokenValidTimeByToken(authToken);
                    String currentTime = DateUtil.getTime();
                    //这个token已作废，加入黑名单
                    log.info("{}已作废，加入黑名单",authToken);
                    redisUtil.addBlackList(authToken);

                    if (DateUtil.compareDate(currentTime, tokenValidTime)) {//超过有效期，不刷新
                        log.info("{}已超过有效期，不刷新",authToken);
                        response.getWriter().println(JSON.toJSONString(ResultUtil.exception(ResultEnum.LOGIN_IS_OVERDUE.getCode(), ResultEnum.LOGIN_IS_OVERDUE.getMessage())));
                        return;
                    } else {//仍在刷新时间内，则刷新token，放入请求头中
                        String usernameByToken = (String) redisUtil.getUsernameByToken(authToken);
                        username = usernameByToken;//更新username

                        clientId = (String) redisUtil.getClientByToken(authToken);//更新ip

                        //获取请求的ip地址
                        Map<String, Object> map = new HashMap<>();
                        map.put("clientId", clientId);
                        String jwtToken = JwtTokenUtil.generateToken(usernameByToken, expirationSeconds, map);

                        //更新redis
                        redisUtil.setTokenRefresh(jwtToken,usernameByToken,clientId);
                        //删除旧的token保存的redis
                        redisUtil.deleteToken(authToken);
                        //新的token保存到redis中
                        redisUtil.setTokenRefresh(jwtToken,username,clientId);

                        log.info("redis已删除旧token：{},新token：{}已更新redis",authToken,jwtToken);
                        authToken = jwtToken;//更新token，为了后面
                        response.setHeader("Authorization", jwtToken);
                    }
                }

            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                /*
                 * 加入对ip的验证
                 * 如果ip不正确，进入黑名单验证
                 */
                if (!StringUtil.equals(clientId, currentIp)) {//地址不正确
                    log.info("用户：{}的ip地址变动，进入黑名单校验",username);
                    //进入黑名单验证
                    if (redisUtil.isBlackList(authToken)) {
                        log.info("用户：{}的token：{}在黑名单之中，拒绝访问",username,authToken);
                        response.getWriter().println(JSON.toJSONString(ResultUtil.exception(ResultEnum.TOKEN_IS_BLACKLIST.getCode(), ResultEnum.TOKEN_IS_BLACKLIST.getMessage())));
                        return;
                    }
                }

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
