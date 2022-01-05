package cn.myauthx.api.web.controller;


import cn.myauthx.api.base.annotation.Admin;
import cn.myauthx.api.base.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-04
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Admin
    @GetMapping("/test2")
    public Result test2(HttpServletRequest request){
        System.out.println("id="+request.getParameter("id"));


        return Result.ok("请求成功");
    }
}
