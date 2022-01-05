package cn.myauthx.api;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class CodeGenerator {

    private String moudleName = "love";//模块名

    private String tableName = "tb_class";//表名

    @Test
    public void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/dbtest?serverTimezone=Asia/Shanghai", "dbtest", "dbtest")
                .globalConfig(builder -> {
                    builder.author("DaenMax") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.demo") // 设置父包名
                            .moduleName(moudleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") +
                                    "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix("tb_") // 设置过滤表前缀
                            .entityBuilder()
                            .enableLombok()
                            .superClass("com.example.demo.base.po.baseEntity")
                            .idType(IdType.AUTO)
                            .enableChainModel()
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle();
                })
                .templateConfig(builder -> {
                    builder.mapperXml("/templates/mapper.xml");
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
