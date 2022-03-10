package cn.myauthx.api.util;

import cn.myauthx.api.main.entity.Card;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 导出XLS文件
 * @author DaenMax
 */
public class ExportXls {


    /**
     * 导出卡密为XLSX
     * 这种是使用的txt方式导出的，不是阿里巴巴的easyexcle
     * @param request
     * @param response
     * @param fileName
     * @param list
     */
    public static void exportCard2Xls(HttpServletRequest request, HttpServletResponse response, String fileName, List<Card> list) {
        request.getSession();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //如果用这种方式导出，WPS或者office打开文件时会检测到是SYLK，即此文件是一个文本文件
        //这样没啥问题，但是如果第一个字符是"ID"，就会有弹窗警告提示，如果用小写的"id"或者其他字符就不会再有弹窗提示
        String ret = "id\t卡密\t点数\t秒数\t生成时间\t使用时间\t使用人账号\t卡密状态\t所属软件\t生成人ID";
        for (Card card : list) {
            ret = ret + "\n" + card.getId() + "\t" + card.getCkey() + "\t" + card.getPoint() + "\t" + card.getSeconds() + "\t" +MyUtils.dateToStr(card.getAddTimeName())+ "\t" +MyUtils.dateToStr(card.getLetTimeName()) + "\t" + card.getLetUser() + "\t" + card.getStatusName() + "\t" + card.getFromSoftName() + "\t" + card.getFromAdminId();
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(ret.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}
