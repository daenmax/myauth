package cn.myauthx.api.main.controller.web;

import cn.myauthx.api.base.annotation.AdminLogin;
import cn.myauthx.api.base.annotation.OpenApi;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Card;
import cn.myauthx.api.main.entity.MyPage;
import cn.myauthx.api.main.service.ICardService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.ExportXls;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 前端web使用的API接口
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class CardController {
    @Resource
    private ICardService cardService;

    /**
     * 导出卡密
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("exportCard")
    public void exportCard(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        if (CheckUtils.isObjectEmpty(card)) {
            return;
        }
        if (CheckUtils.isObjectEmpty(card.getFromSoftId())) {
            return;
        }
        List<Card> cardList = cardService.exportCard(card);
        ExportXls.exportXls(request, response, "exportCard", "卡密", cardList,Card.class);
        return;
    }

    /**
     * 获取卡密列表
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getCardList")
    public Result getMsgList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(card) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(card.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return cardService.getCardList(card, myPage);
    }

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("getCard")
    public Result getCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(card.getId()) && CheckUtils.isObjectEmpty(card.getCkey())) {
            return Result.error("id和ckey不能都为空");
        }
        return cardService.getCard(card);
    }

    /**
     * 修改卡密
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("updCard")
    public Result updCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(card.getId())) {
            return Result.error("id不能为空");
        }
        card.setStatus(null);
        card.setLetTime(null);
        card.setLetUser(null);
        card.setFromSoftId(null);
        card.setAddTime(null);
        if (CheckUtils.isObjectEmpty(card.getCkey()) && CheckUtils.isObjectEmpty(card.getPoint())
                && CheckUtils.isObjectEmpty(card.getSeconds())) {
            return Result.error("参数不能全部为空");
        }
        return cardService.updCard(card);
    }

    /**
     * 生成卡密
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("addCard")
    public Result addCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        String prefix = jsonObject.getString("prefix");
        Integer count = jsonObject.getInteger("count");
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(count)) {
            return Result.error("count参数不能为空");
        }
        if (count == 0) {
            return Result.error("生成张数不能为0");
        }
        if (CheckUtils.isObjectEmpty(card.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(card.getSeconds()) && CheckUtils.isObjectEmpty(card.getPoint())) {
            return Result.error("seconds和point不能都为空");
        }
        if (CheckUtils.isObjectEmpty(card.getSeconds())) {
            card.setSeconds(0);
        }
        if (CheckUtils.isObjectEmpty(card.getPoint())) {
            card.setPoint(0);
        }
        return cardService.addCard(prefix, count, card);
    }

    /**
     * 删除卡密（支持批量）
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("delCard")
    public Result delCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return cardService.delCard(ids);
    }

    /**
     * 禁用卡密（支持批量）
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("banCard")
    public Result banCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return cardService.banCard(ids);
    }

    /**
     * 解禁卡密（支持批量）
     *
     * @param request
     * @return
     */
    @OpenApi
    @AdminLogin
    @PostMapping("unBanCard")
    public Result unBanCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return cardService.unBanCard(ids);
    }

}
