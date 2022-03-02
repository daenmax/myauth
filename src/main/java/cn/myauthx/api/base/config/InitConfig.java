package cn.myauthx.api.base.config;

import cn.myauthx.api.main.entity.*;
import cn.myauthx.api.main.enums.AdminEnums;
import cn.myauthx.api.main.service.*;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.MyUtils;
import cn.myauthx.api.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 项目初始化类
 * @author DaenMax
 */
@Slf4j
@Configuration
public class InitConfig  implements ApplicationRunner {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ISoftService softService;
    @Resource
    private IAdminService adminService;
    @Resource
    private IVersionService versionService;
    @Resource
    private IBanService banService;
    @Resource
    private IConfigService configService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=================  【启动服务成功，开始初始化服务】  =================");
        log.info("[redis]开始执行清空操作...");
        Set<String> scan = redisUtil.scan("ban*");
        for (String s : scan) {
            redisUtil.del(s.toString());
        }
        /*scan = redisUtil.scan("id*");
        for (String s : scan) {
            redisUtil.del(s.toString());
        }
        scan = redisUtil.scan("soft*");
        for (String s : scan) {
            redisUtil.del(s.toString());
        }
        scan = redisUtil.scan("version*");
        for (String s : scan) {
            redisUtil.del(s.toString());
        }*/
        log.info("[redis]清空完毕");
        log.info("[config]初始化配置表...");
        Config config = configService.getById(1);
        if(CheckUtils.isObjectEmpty(config)){
            Config config1 = new Config();
            config1.setId(1);
            config1.setSeoTitle("MyAuth");
            config1.setSeoKeywords("MyAuth授权管理系统;免费授权系统");
            config1.setSeoDescription("一款稳定 高效 免费的授权管理系统");
            config1.setOpenApiKey(MyUtils.getUUID(false));
            config = config1;
            configService.save(config1);
        }
        redisUtil.set("config" ,config);
        log.info("[config]配置表初始化完毕");

        log.info("[admin]初始化管理员...");
        long adminCount = adminService.count();
        if(adminCount == 0){
            Admin admin = new Admin();
            admin.setUser("admin");
            admin.setPass("123456");
            admin.setRegTime(Integer.valueOf(MyUtils.getTimeStamp()));
            admin.setStatus(AdminEnums.STATUS_ABLE.getCode());
            adminService.save(admin);
        }
        log.info("[admin]管理员初始化完毕");

        log.info("[soft]开始读取...");
        List<Soft> softList = softService.list();
        log.info("[soft]读取到的数量->" + softList.size());
        for (Soft soft : softList) {
            redisUtil.set("soft:" + soft.getSkey(),soft);
            redisUtil.set("id:soft:" + soft.getId(),soft);
            log.info("[soft]添加到Redis->" + soft.toString());
            log.info("[version][" + soft.getName() + "]开始读取版本列表...");
            LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            versionLambdaQueryWrapper.eq(Version::getFromSoftId,soft.getId());
            List<Version> versionList = versionService.list(versionLambdaQueryWrapper);
            log.info("[version][" + soft.getName() + "]读取到的版本数量->" + versionList.size());
            for (Version version : versionList) {
                redisUtil.set("version:" + version.getVkey(),version);
                redisUtil.set("id:version:" + version.getId(),version);
                log.info("[version][" + soft.getName() + "]添加到Redis->" + version.getVer());
            }
            log.info("[version][" + soft.getName() + "]添加到Redis完成");
        }
        log.info("[soft]添加到Redis完成");



        log.info("[ban]开始读取...");
        List<Ban> banList = banService.list();
        log.info("[ban]读取到的数量->" + banList.size());
        for (Ban ban : banList) {
            if(ban.getToTime().equals(-1)){
                redisUtil.set("ban:" + ban.getValue() +"-" + ban.getType() + "-" + ban.getFromSoftId(),ban);
            }else{
                Integer seconds = ban.getToTime() - Integer.parseInt(MyUtils.getTimeStamp());
                if(seconds > 0){
                    redisUtil.set("ban:" + ban.getValue() +"-" + ban.getType() + "-" + ban.getFromSoftId(),ban,seconds);
                }
            }
            log.info("[ban]添加到Redis->" + ban.toString());
        }
        log.info("[ban]添加到Redis完成");

        log.info("\n███████╗████████╗ █████╗ ██████╗ ████████╗    ███████╗██╗   ██╗ ██████╗ ██████╗███████╗███████╗███████╗███████╗██╗   ██╗██╗     \n" +
                "██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝    ██╔════╝██║   ██║██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝██║   ██║██║     \n" +
                "███████╗   ██║   ███████║██████╔╝   ██║       ███████╗██║   ██║██║     ██║     █████╗  ███████╗███████╗█████╗  ██║   ██║██║     \n" +
                "╚════██║   ██║   ██╔══██║██╔══██╗   ██║       ╚════██║██║   ██║██║     ██║     ██╔══╝  ╚════██║╚════██║██╔══╝  ██║   ██║██║     \n" +
                "███████║   ██║   ██║  ██║██║  ██║   ██║       ███████║╚██████╔╝╚██████╗╚██████╗███████╗███████║███████║██║     ╚██████╔╝███████╗\n" +
                "╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝       ╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝╚══════╝╚══════╝╚══════╝╚═╝      ╚═════╝ ╚══════╝\n" +
                "                                                                                                                                ");
    }
}

