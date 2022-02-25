package cn.myauthx.api.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 导出XLS文件
 * @author DaenMax
 */
public class ExportXls<T> {
    /**
     * 导出XLS
     *
     * @param request
     * @param response
     * @param fileName 导出的文件名，不需要加.xls
     * @param sheetName 工作表名
     * @param list<T>
     */
    public static <T> void exportXls(HttpServletRequest request, HttpServletResponse response, String fileName, String sheetName, List<T> list) {
        request.getSession();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExcelWriterBuilder write = EasyExcel.write(outputStream, list.getClass());
        ExcelWriterSheetBuilder sheet = write.sheet(sheetName);
        sheet.doWrite(list);
        return;
    }
}
