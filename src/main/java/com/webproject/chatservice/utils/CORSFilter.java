package com.webproject.chatservice.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author ljo
 *
 */

public class CORSFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "http://hyunee.shop.s3-website.ap-northeast-2.amazonaws.com");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers","*");
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }

}
