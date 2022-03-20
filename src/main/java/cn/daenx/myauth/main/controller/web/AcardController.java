package cn.daenx.myauth.main.controller.web;


import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Acard;
import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.main.entity.Role;
import cn.daenx.myauth.main.service.IAcardService;
import cn.daenx.myauth.main.service.IAdminService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.ExportXls;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-16
 */
@RestController
@RequestMapping("/web")
public class AcardController {
    @Resource
    private IAcardService acardService;
    @Resource
    private IAdminService adminService;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取卡密列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getACardList")
    public Result getACardList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Acard acard = jsonObject.toJavaObject(Acard.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(acard) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return acardService.getACardList(acard, myPage);
    }

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getACard")
    public Result getACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Acard acard = jsonObject.toJavaObject(Acard.class);
        if (CheckUtils.isObjectEmpty(acard)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(acard.getId()) && CheckUtils.isObjectEmpty(acard.getCkey())) {
            return Result.error("id和ckey不能都为空");
        }
        return acardService.getACard(acard);
    }

    /**
     * 修改卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("updACard")
    public Result updACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Acard acard = jsonObject.toJavaObject(Acard.class);
        if (CheckUtils.isObjectEmpty(acard)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(acard.getId())) {
            return Result.error("id不能为空");
        }
        acard.setStatus(null);
        acard.setLetTime(null);
        acard.setLetUser(null);
        acard.setAddTime(null);
        if (CheckUtils.isObjectEmpty(acard.getCkey()) && CheckUtils.isObjectEmpty(acard.getMoney())) {
            return Result.error("参数不能全部为空");
        }
        return acardService.updACard(acard);
    }

    /**
     * 生成卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("addACard")
    public Result addACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Acard acard = jsonObject.toJavaObject(Acard.class);
        String prefix = jsonObject.getString("prefix");
        Integer count = jsonObject.getInteger("count");
        if (CheckUtils.isObjectEmpty(acard)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(count)) {
            return Result.error("count参数不能为空");
        }
        if (count == 0) {
            return Result.error("生成张数不能为0");
        }
        if (CheckUtils.isObjectEmpty(acard.getMoney())) {
            return Result.error("money不能为空");
        }
        Admin myAdmin = (Admin) request.getAttribute("obj_admin");
        return acardService.addACard(prefix, count, acard, myAdmin);
    }

    /**
     * 删除卡密（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("delACard")
    public Result delACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return acardService.delACard(ids);
    }

    /**
     * 禁用卡密（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("banACard")
    public Result banACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return acardService.banACard(ids);
    }

    /**
     * 解禁卡密（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("unBanACard")
    public Result unBanACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return acardService.unBanACard(ids);
    }

    /**
     * 导出卡密
     *
     * @param ckey
     * @param money
     * @param addTime
     * @param letTime
     * @param letUser
     * @param status
     * @param request
     * @param response
     * @throws IOException
     */
    @NoEncryptNoSign
    @GetMapping("exportACard")
    public void exportACard(String token, String ckey, String money, Integer addTime, Integer letTime, String letUser, Integer status, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Admin admin = adminService.tokenIsOk(token);
        if (CheckUtils.isObjectEmpty(admin)) {
            return;
        }
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId() != 0) {
            return;
        }
        Acard acard = new Acard();
        acard.setCkey(ckey);
        acard.setMoney(money);
        acard.setAddTime(addTime);
        acard.setLetTime(letTime);
        acard.setLetUser(letUser);
        acard.setStatus(status);
        if (CheckUtils.isObjectEmpty(acard)) {
            return;
        }
        List<Acard> acardList = acardService.exportACard(acard);
        ExportXls.exportACard2Xls(request, response, "exportACard", acardList);
        return;
    }

    /**
     * 使用代理卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("letACard")
    public Result letACard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ckey = jsonObject.getString("ckey");
        if (CheckUtils.isObjectEmpty(ckey)) {
            return Result.error("ckey参数不能为空");
        }
        Admin myAdmin = (Admin) request.getAttribute("obj_admin");
        return acardService.letACard(ckey, myAdmin);
    }
}
