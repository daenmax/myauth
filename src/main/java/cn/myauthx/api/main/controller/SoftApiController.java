package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.vo.Result;


import cn.myauthx.api.util.CheckUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 软件使用API接口
 * @author DaenMax
 */
@RestController
@RequestMapping("/soft")
public class SoftApiController {
    @PostMapping("/test")
    public Result test(@RequestBody Map<String,Object> map){
        Object id = map.get("id");

        if(CheckUtils.isObjectEmpty(id)){
            return Result.error("id不能为空");
        }
        return Result.ok("请求成功");
    }
}
