package com.neusoft.bookstore.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

        @Override
        public void destroy() {

        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse res,
                FilterChain chain) throws IOException, ServletException {
                HttpServletResponse response = (HttpServletResponse) res;
                //允许所有请求跨域返回
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Max-Age", "3600");
//                response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
                response.setHeader("Access-Control-Allow-Headers", "*");
                chain.doFilter(req, res);

        }

        @Override
        public void init(FilterConfig arg0) throws ServletException {

        }
        }
      
