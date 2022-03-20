package cn.daenx.myauth.main.mapper;

import cn.daenx.myauth.main.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 获取菜单列表
     *
     * @param menu
     * @return
     */
    List<Menu> treeList(Menu menu);
}
