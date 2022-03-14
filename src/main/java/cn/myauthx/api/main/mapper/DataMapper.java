package cn.myauthx.api.main.mapper;

import cn.myauthx.api.base.vo.DataRanking;
import cn.myauthx.api.main.entity.Data;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Mapper
public interface DataMapper extends BaseMapper<Data> {
    List<DataRanking> getDataRanking(DataRanking dataRanking);
}
