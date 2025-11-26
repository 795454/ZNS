package huang.jin.hua.controller;


import huang.jin.hua.intercepter.LoginUser;
import huang.jin.hua.utils.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class UserController {

    @Resource
    private AuthenticationManager authenticationManager;

    @GetMapping(value={"/login"})
    public ModelAndView login(){
        ModelAndView view = new ModelAndView("login");
        return view;
    }

    @PostMapping("/login")
    public ModelAndView login(String username, String password, HttpSession session) {
        ModelAndView view = new ModelAndView("login");
        // 根据用户名及密码创建未认证的Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // 暂时保存authenticationToken,以便在MyUserDetailsService中验证密码时取出密码
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        try {
            // 验证未认证Authentication后返回一个已认证Authentication
            // authenticate方法执行后自动调用MyUserDetailsService中的loadUserByUsername 进行用户名及密码验证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 保存Authentication，也可用Redis保存
            session.setAttribute("AUTH", authentication);

//            // 获取角色
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // 创建token返回给请求端
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            session.setAttribute(Constants.USER, loginUser.getUser());
            System.out.println("登录用户:" + loginUser.getUser().getUsername());

            view.setViewName("redirect:/product");

        }catch (Exception e){
            SecurityContextHolder.clearContext();
            e.printStackTrace();
            throw e;
        }
        return view;
    }

    /**
     * 用户登出
     * @return
     */
    @GetMapping("/logout")
    public ModelAndView Logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(request, response, (Authentication)request.getSession().getAttribute("AUTH"));

        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/index");

        return view;
    }



}
