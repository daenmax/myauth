package cn.myauthx.api.base.config;

import cn.myauthx.api.main.entity.Admin;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.service.IAdminService;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
import java.util.List;

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
    private ISoftService iSoftService;
    @Resource
    private IAdminService iAdminService;
    @Resource
    private IVersionService iVersionService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[soft]开始读取...");
        List<Soft> softList = iSoftService.list();
        log.info("[soft]读取到的数量->" + softList.size());
        for (Soft soft : softList) {
            redisUtil.set("soft_" + soft.getSkey(),soft);
            log.info("[soft]添加到Redis->" + soft.toString());
            log.info("[version][" + soft.getName() + "]开始读取版本列表...");
            LambdaQueryWrapper<Version> versionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            versionLambdaQueryWrapper.eq(Version::getFromSoftId,soft.getId());
            List<Version> versionList = iVersionService.list(versionLambdaQueryWrapper);
            log.info("[version][" + soft.getName() + "]读取到的版本数量->" + versionList.size());
            for (Version version : versionList) {
                redisUtil.set("version_" + version.getVkey(),version);
                log.info("[version][" + soft.getName() + "]添加到Redis->" + version.getVer());
            }
            log.info("[version][" + soft.getName() + "]添加到Redis完成");
        }
        log.info("[soft]添加到Redis完成");

        log.info("[admin]开始读取...");
        List<Admin> adminList = iAdminService.list();
        log.info("[admin]读取到的数量->" + adminList.size());
        for (Admin admin : adminList) {
            redisUtil.set("admin_" + admin.getToken(),admin);
            log.info("[admin]添加到Redis->" + admin.toString());
        }
        log.info("[admin]添加到Redis完成");

        log.info("\n███████╗████████╗ █████╗ ██████╗ ████████╗    ███████╗██╗   ██╗ ██████╗ ██████╗███████╗███████╗███████╗███████╗██╗   ██╗██╗     \n" +
                "██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝    ██╔════╝██║   ██║██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝██║   ██║██║     \n" +
                "███████╗   ██║   ███████║██████╔╝   ██║       ███████╗██║   ██║██║     ██║     █████╗  ███████╗███████╗█████╗  ██║   ██║██║     \n" +
                "╚════██║   ██║   ██╔══██║██╔══██╗   ██║       ╚════██║██║   ██║██║     ██║     ██╔══╝  ╚════██║╚════██║██╔══╝  ██║   ██║██║     \n" +
                "███████║   ██║   ██║  ██║██║  ██║   ██║       ███████║╚██████╔╝╚██████╗╚██████╗███████╗███████║███████║██║     ╚██████╔╝███████╗\n" +
                "╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝       ╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝╚══════╝╚══════╝╚══════╝╚═╝      ╚═════╝ ╚══════╝\n" +
                "                                                                                                                                ");
    }
}

