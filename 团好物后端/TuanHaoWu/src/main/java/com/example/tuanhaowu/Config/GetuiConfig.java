package com.example.tuanhaowu.Config;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.StatisticApi;
import com.getui.push.v2.sdk.api.UserApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetuiConfig {
    @Value("${getui.domain}")
    private String domain;

    @Value("${getui.appId}")
    private String appId;

    @Value("${getui.appKey}")
    private String appKey;

    @Value("${getui.masterSecret}")
    private String masterSecret;

    @Bean(name = "pushAPI")
    public PushApi pushApi() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(appKey);
        apiConfiguration.setMasterSecret(masterSecret);
        apiConfiguration.setDomain(domain);
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper.creatApi(PushApi.class);
    }

    @Bean(name = "userAPI")
    public UserApi userApi() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(appKey);
        apiConfiguration.setMasterSecret(masterSecret);
        apiConfiguration.setDomain(domain);
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper.creatApi(UserApi.class);
    }

    @Bean(name = "statisticAPI")
    public StatisticApi statisticApi() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(appKey);
        apiConfiguration.setMasterSecret(masterSecret);
        apiConfiguration.setDomain(domain);
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper.creatApi(StatisticApi.class);
    }
}
