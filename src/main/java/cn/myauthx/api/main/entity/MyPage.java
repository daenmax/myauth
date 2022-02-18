package cn.myauthx.api.main.entity;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class MyPage {
    private Integer pageIndex;
    private Integer pageSize;
    private List<OrderItem> orders;
}
