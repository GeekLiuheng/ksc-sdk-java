package com.ksc.cdn.model.domain.blockurl;

import com.ksc.cdn.KscClientException;
import com.ksc.cdn.model.GeneralRequest;
import com.ksc.cdn.model.GeneralRequestParam;
import com.ksc.cdn.model.valid.FieldValidate;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 屏蔽url请求参数
 * Created by YANGXUEYI on 2017/7/18.
 */
public class BlockDomainUrlRequest implements GeneralRequest {

    /**
     * 操作接口名，系统规定参数 取值：block：屏蔽URL；unblock：解除屏蔽；
     */
    @FieldValidate
    private String BlockType;

    /**
     * URL列表
     */
    @FieldValidate
    private List<TaskUrl> Urls;

    /**
     * (仅在解除屏蔽时生效)，解除屏蔽时，是否联动触发刷新任务 取值：on：触发刷新任务、off：不触发刷新任务 ，默认为off
     */
    private String RefreshOnUnblock;

    /**
     *
     */
    private Long BlockTime;

    @Override
    public GeneralRequestParam getGeneralRequestParam() {
        return new GeneralRequestParam("BlockDomainUrl", "2016-09-01", "/2016-09-01/content/BlockDomainUrl");
    }

    @Override
    public Map<String, String> buildParams() throws KscClientException, ParseException {
        return null;
    }

    public String getBlockType() {
        return BlockType;
    }

    public void setBlockType(String blockType) {
        BlockType = blockType;
    }

    public List<TaskUrl> getUrls() {
        return Urls;
    }

    public void setUrls(List<TaskUrl> urls) {
        Urls = urls;
    }

    public String getRefreshOnUnblock() {
        return RefreshOnUnblock;
    }

    public void setRefreshOnUnblock(String refreshOnUnblock) {
        RefreshOnUnblock = refreshOnUnblock;
    }

    public Long getBlockTime() {
        return BlockTime;
    }

    public void setBlockTime(Long blockTime) {
        BlockTime = blockTime;
    }
}
