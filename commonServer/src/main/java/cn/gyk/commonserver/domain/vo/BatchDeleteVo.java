package cn.gyk.commonserver.domain.vo;


import cn.gyk.commonserver.domain.dto.DeleteParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
 * 接收删除的ID列表
 */
public class BatchDeleteVo {
    private List<DeleteParams> batchDeleteListId;
}
