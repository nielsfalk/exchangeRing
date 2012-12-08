package de.hh.changeRing.jsfExtension;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: niles
 * Date: 08.12.12
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class EnableUtf8Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
    }
}
