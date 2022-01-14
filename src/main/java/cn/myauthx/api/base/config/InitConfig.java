package cn.myauthx.api.base.config;

import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.mapper.SoftMapper;
import cn.myauthx.api.main.service.ISoftService;
import cn.myauthx.api.util.RedisUtil;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[soft]开始读取数据库");
        List<Soft> softList = iSoftService.list();
        log.info("[soft]读到的列表数量->" + softList.size());
        for (Soft soft : softList) {
            redisUtil.set(soft.getSkey(),soft);
            log.info("[soft]添加到Redis->" + soft.toString());
        }
        log.info("[soft]添加到Redis完成");
        log.info("\n███████╗████████╗ █████╗ ██████╗ ████████╗    ███████╗██╗   ██╗ ██████╗ ██████╗███████╗███████╗███████╗███████╗██╗   ██╗██╗     \n" +
                "██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝    ██╔════╝██║   ██║██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝██╔════╝██║   ██║██║     \n" +
                "███████╗   ██║   ███████║██████╔╝   ██║       ███████╗██║   ██║██║     ██║     █████╗  ███████╗███████╗█████╗  ██║   ██║██║     \n" +
                "╚════██║   ██║   ██╔══██║██╔══██╗   ██║       ╚════██║██║   ██║██║     ██║     ██╔══╝  ╚════██║╚════██║██╔══╝  ██║   ██║██║     \n" +
                "███████║   ██║   ██║  ██║██║  ██║   ██║       ███████║╚██████╔╝╚██████╗╚██████╗███████╗███████║███████║██║     ╚██████╔╝███████╗\n" +
                "╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝       ╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝╚══════╝╚══════╝╚══════╝╚═╝      ╚═════╝ ╚══════╝\n" +
                "                                                                                                                                ");
    }
}

