package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 前端web使用的API接口
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class VersionController {
    @Resource
    private IVersionService versionService;

    /**
     * 获取软件列表
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getVersionList")
    public Result getVersionList(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Version version = jsonObject.toJavaObject(Version.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        String skey = jsonObject.getString("skey");
        if(CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())){
            return Result.error("页码和尺寸参数不能为空");
        }

        if(CheckUtils.isObjectEmpty(skey)){
            return Result.error("skey参数不能为空");
        }
        return versionService.getVersionList(skey,version,myPage);
    }

    /**
     * 查询版本，根据id或者skey
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getVersion")
    public Result getVersion(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Version version = jsonObject.toJavaObject(Version.class);
        if(CheckUtils.isObjectEmpty(version)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(version.getId()) && CheckUtils.isObjectEmpty(version.getVkey())){
            return Result.error("id和vkey不能全部为空");
        }
        return versionService.getVersion(version);
    }

    /**
     * 修改软件
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updVersion")
    public Result updVersion(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Version version = jsonObject.toJavaObject(Version.class);
        if(CheckUtils.isObjectEmpty(version)){
            return Result.error("参数错误");
        }
        version.setVkey(null);
        version.setFromSoftId(null);
        version.setUpdTime(null);
        if(CheckUtils.isObjectEmpty(version.getId())){
            return Result.error("id不能为空");
        }
        if(CheckUtils.isObjectEmpty(version.getVer()) && CheckUtils.isObjectEmpty(version.getUpdLog())
                && CheckUtils.isObjectEmpty(version.getUpdType()) && CheckUtils.isObjectEmpty(version.getStatus())){
            return Result.error("参数不能全部为空");
        }
        return versionService.updVersion(version);
    }

    /**
     * 添加版本
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addVersion")
    public Result addVersion(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Version version = jsonObject.toJavaObject(Version.class);
        if(CheckUtils.isObjectEmpty(version)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(version.getVer()) || CheckUtils.isObjectEmpty(version.getFromSoftId()) || CheckUtils.isObjectEmpty(version.getUpdLog())
                || CheckUtils.isObjectEmpty(version.getUpdType()) || CheckUtils.isObjectEmpty(version.getStatus())){
            return Result.error("参数不全");
        }
        return versionService.addVersion(version);
    }
    /**
     * 删除版本，会同步删除用户、数据、回复、日志
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delVersion")
    public Result delVersion(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Version version = jsonObject.toJavaObject(Version.class);
        if(CheckUtils.isObjectEmpty(version)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(version.getId())){
            return Result.error("id不能为空");
        }
        return versionService.delVersion(version);
    }

}
