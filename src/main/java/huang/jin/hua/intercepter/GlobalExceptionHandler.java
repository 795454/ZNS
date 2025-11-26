package huang.jin.hua.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String loginExceptionHandler(Exception e, Model model,
                                        HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("loginExceptionHandler");
        String errMessage = e.getMessage();
        System.out.println("loginExceptionHandler:" + errMessage);

        if(errMessage.contains("user not exist")) {
            errMessage = "用户不存在";
        }

        if(errMessage.contains("password error")) {
            errMessage = "密码错误";
        }

        model.addAttribute("error", errMessage);
        return "login";
    }

}
