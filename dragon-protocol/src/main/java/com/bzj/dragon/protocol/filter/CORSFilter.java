package com.bzj.dragon.protocol.filter;


import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决跨域问题拦截器
 * User:aaronbai@tcl.com
 * Date:2016-10-14
 * Time:17:33
 */
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        if(request.getHeader("Referer") != null && request.getHeader("Referer").indexOf("apidoc")<0){
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(request.getHeader("Referer")).build();
            String origin = uriComponents.getScheme() + "://" + uriComponents.getHost();
            if(uriComponents.getPort() > 0){
                origin = origin +  ":" + uriComponents.getPort();
            }
            response.setHeader("Access-Control-Allow-Origin", origin);
            //        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            //        response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, X-Atmosphere-tracking-id, X-Atmosphere-Framework, X-Cache-Date, Content-Type, X-Atmosphere-Transport,Sign-Auth, *");
            //        response.setHeader("Access-Control-Request-Headers",
            //                           "Origin, X-Atmosphere-tracking-id, X-Atmosphere-Framework, X-Cache-Date, Content-Type, X-Atmosphere-Transport,  *");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            //        response.addHeader("P3P", "CP=CAO PSA OUR");
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

}
