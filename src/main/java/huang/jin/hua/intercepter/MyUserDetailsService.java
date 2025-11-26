package huang.jin.hua.intercepter;


import huang.jin.hua.pojo.User;
import huang.jin.hua.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("MyUserDetailsService-loadUserByUsername");

        // 模拟从数据库查询得到用户数据,密码明文:admin123
        // User user = new User("admin", "$2a$10$OM.5fbLennA6nylSHGtA2qdubu5FLaWOG5TZsjw9wYWWR5iKYglS");
        User user = userService.getUserByUsername(username);
        // 验证用户名
        if(user == null) {
            log.info("登录用户: {} 不存在", username);
            throw new RuntimeException("user not exist");
        }

        // 以下验证密码，DaoAuthenticationProvider会再一次验证，可省略
        Authentication usernamePasswordAuthenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 用户输入的登录密码
        String rawPassword = usernamePasswordAuthenticationToken.getCredentials().toString();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(rawPassword, user.getPassword())){
            log.info("登录用户：密码错误：");
            throw new RuntimeException("password error.");
        }

        // 以下可选，添加用户角色
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); //authorities

        return new LoginUser(user.getUsername(), user.getPassword(), user, authorities);
    }

}
