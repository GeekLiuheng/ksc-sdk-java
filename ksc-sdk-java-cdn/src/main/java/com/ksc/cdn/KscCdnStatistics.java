package com.ksc.cdn;

import com.ksc.cdn.model.statistic.*;
import com.ksc.cdn.model.statistic.bandwidth.BpsRequest;
import com.ksc.cdn.model.statistic.bandwidth.BpsResult;
import com.ksc.cdn.model.statistic.flow.DomainRankingRequest;
import com.ksc.cdn.model.statistic.flow.DomainRankingResult;
import com.ksc.cdn.model.statistic.flow.FlowRequest;
import com.ksc.cdn.model.statistic.flow.FlowResult;
import com.ksc.cdn.model.statistic.hitrate.HitRateDetailRequest;
import com.ksc.cdn.model.statistic.hitrate.HitRateDetailResult;
import com.ksc.cdn.model.statistic.hitrate.HitRateRequest;
import com.ksc.cdn.model.statistic.hitrate.HitRateResult;
import com.ksc.cdn.model.statistic.httpcode.HttpCodeDetailRequest;
import com.ksc.cdn.model.statistic.httpcode.HttpCodeDetailResult;
import com.ksc.cdn.model.statistic.httpcode.HttpCodeRequest;
import com.ksc.cdn.model.statistic.httpcode.HttpCodeResult;
import com.ksc.cdn.model.statistic.province.isp.ProvinceAndIspRequest;
import com.ksc.cdn.model.statistic.province.isp.bandwidth.ProvinceAndIspBandwidthResult;
import com.ksc.cdn.model.statistic.province.isp.flow.ProvinceAndIspFlowResult;
import com.ksc.cdn.model.statistic.pv.PVRequest;
import com.ksc.cdn.model.statistic.pv.PVResult;

/**
 * KscCdnStatistics
 * 统计相关接口
 * Created by jiangran@kingsoft.com on 03/11/2016.
 */
public interface KscCdnStatistics {
    /**
     * 带宽查询
     */
    String BANDWIDTH_URL = "/2016-09-01/statistics/GetBandwidthData";
    String BANDWIDTH_VERSION = "2016-09-01";
    String BANDWIDTH_ACTION = "GetBandwidthData";

    /**
     * 流量查询
     */
    String FLOW_URL = "/2016-09-01/statistics/GetFlowData";
    String FLOW_VERSION = "2016-09-01";
    String FLOW_ACTION = "GetFlowData";
    /**
     * pv查询
     */
    String PV_URL = "/2016-09-01/statistics/GetPvData";
    String PV_VERSION = "2016-09-01";
    String PV_ACTION = "GetPvData";

    /**
     * 命中率查询
     */
    String HITRATE_URL = "/2016-09-01/statistics/GetHitRateData";
    String HITRATE_VERSION = "2016-09-01";
    String HITRATE_ACTION = "GetHitRateData";
    /**
     * 命中率详情查询
     */
    String HITRATE_DETAIL_URL = "/2016-09-01/statistics/GetHitRateDetailedData";
    String HITRATE_DETAIL_VERSION = "2016-09-01";
    String HITRATE_DETAIL_ACTION = "GetHitRateDetailedData";
    /**
     * 域名排行查询
     */
    String FLOW_RANK_URL = "/2016-09-01/statistics/GetDomainRankingListData";
    String FLOW_RANK_VERSION = "2016-09-01";
    String FLOW_RANK_ACTION = "GetDomainRankingListData";
    /**
     * 省份+运营商流量查询
     */
    String PROVINCE_ISP_FLOW_URL="/2016-09-01/statistics/GetProvinceAndIspFlowData";
    String PROVINCE_ISP_FLOW_VERSION="2016-09-01";
    String PROVINCE_ISP_FLOW_ACTION="GetProvinceAndIspFlowData";
    /**
     * 省份+运营商带宽查询
     */
    String PROVINCE_ISP_BW_URL="/2016-09-01/statistics/GetProvinceAndIspBandwidthData";
    String PROVINCE_ISP_BW_VERSION="2016-09-01";
    String PROVINCE_ISP_BW_ACTION="GetProvinceAndIspBandwidthData";
    /**
     * 状态码统计
     */
    String HTTPCODE_URL="/2016-09-01/statistics/GetHttpCodeData";
    String HTTPCODE_VERSION="2016-09-01";
    String HTTPCODE_ACTION="GetHttpCodeData";
    /**
     * 状态码详情统计
     */
    String HTTPCODE_DETAIL_URL="/2016-09-01/statistics/GetHttpCodeDetailedData";
    String HTTPCODE_DETAIL_VERSION="2016-09-01";
    String HTTPCODE_DETAIL_ACTION="GetHttpCodeDetailedData";
    /**
     * 查询带宽
     * @param statisticsQuery
     * @return
     * @throws Exception
     */
    BpsResult getBandwidthData(BpsRequest statisticsQuery) throws Exception;

    /**
     * 查询流量
     * @param statisticsQuery
     * @return
     * @throws Exception
     */
    FlowResult getFlowDataByApi(FlowRequest statisticsQuery) throws Exception;

    /**
     * 命中率查询
     * @param hitRateRequest
     * @return
     * @throws Exception
     */
    HitRateResult getHitRate(HitRateRequest hitRateRequest) throws Exception;

    /**
     * 命中率详情查询
     * @param statisticsQuery
     * @return
     * @throws Exception
     */
    HitRateDetailResult getHitRateDetail(HitRateDetailRequest statisticsQuery) throws Exception;

    /**
     * 请求数查询
     * @param statisticsQuery
     * @return
     * @throws Exception
     */
    PVResult getPV(PVRequest statisticsQuery) throws Exception;

    /**
     * 域名排行查询
     * 获取用户维度下所有域名的流量、流量占比、带宽峰值、峰值时间、访问次数，并按流量排行
     * 单天维度，仅指定的单天时间查询
     * @param domainRankingRequest
     * @return
     */
    DomainRankingResult getDomainRankingList(DomainRankingRequest domainRankingRequest) throws Exception;

    /**
     * 获取域名在中国大陆地区各省份及各运营商的流量数据，仅包括边缘节点数据，单位:byte
     * @param provinceAndIspRequest
     * @return
     * @throws Exception
     */
    ProvinceAndIspFlowResult getProvinceAndIspFlow(ProvinceAndIspRequest provinceAndIspRequest) throws Exception;

    /**
     * 获取域名在中国大陆地区各省市及各运营商的带宽数据，仅包括边缘节点数据，单位:bit/second
     * @return
     * @throws Exception
     */
    ProvinceAndIspBandwidthResult getProvinceAndIspBW(ProvinceAndIspRequest provinceAndIspRequest) throws Exception;

    /**
     * 获取域名一段时间内的Http状态码访问次数及占比数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    HttpCodeResult getHttpCodeData(HttpCodeRequest request) throws Exception;

    /**
     * 获取域名的Http状态码详细访问次数及占比数据
     * @param request
     * @return
     * @throws Exception
     */
    HttpCodeDetailResult getHttpCodeDetailedData(HttpCodeDetailRequest request) throws Exception;
}
