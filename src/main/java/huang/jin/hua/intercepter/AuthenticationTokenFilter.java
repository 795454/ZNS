package huang.jin.hua.intercepter;

import huang.jin.hua.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    /**
     * 判断用户是否已登录
     * @param session
     * @return
     */
    private boolean isLoggedIn(HttpSession session) {
        Object user = session.getAttribute(Constants.USER);
        return user != null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean res = isLoggedIn(request.getSession());
        if(!res) {
            // 允许permitAll中的设置
            filterChain.doFilter(request, response);
            return;
        }

        // 从Session取出来给Security验证
        SecurityContextHolder.getContext().setAuthentication((Authentication)request.getSession().getAttribute("AUTH"));

        filterChain.doFilter(request, response);

    }

}