package cn.myauthx.api.web.service.impl;

import cn.myauthx.api.web.entity.Card;
import cn.myauthx.api.web.mapper.CardMapper;
import cn.myauthx.api.web.service.ICardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements ICardService {

}
