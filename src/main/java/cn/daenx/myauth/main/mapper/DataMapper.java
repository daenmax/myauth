package cn.daenx.myauth.main.mapper;

import cn.daenx.myauth.base.vo.DataRanking;
import cn.daenx.myauth.main.entity.Data;
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
public interface DataMapper extends BaseMapper<Data> {
    /**
     * 获取数据排行
     *
     * @param dataRanking
     * @return
     */
    List<DataRanking> getDataRanking(DataRanking dataRanking);

    /**
     * 获取数据排行_数量
     *
     * @param dataRanking
     * @return
     */
    Integer getDataRankingCount(DataRanking dataRanking);
}
