package com.example.tuanhaowu.util;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.AudienceDTO;
import com.getui.push.v2.sdk.dto.req.Settings;
import com.getui.push.v2.sdk.dto.req.Strategy;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class PushUtil {
    @Resource(name = "pushAPI")
    private PushApi pushApi;

    // 使用cid向特定的一个用户推送消息，title为通知栏消息的标题，content为通知栏消息的内容，payload为任何额外需要的信息，
    // 比如点击推送消息后APP要跳转到的界面，界面的参数等等，具体形式与前端页面和解析有关，有待商定
    // 示例: pushUtil.pushToSingle("8b3c2b3b05e67d0d499502bb23d86dd5", "团购开始通知", "您订阅的团购蔬果每日将在30分钟后开始", "/goods_view?groupId=1045");
    public void pushToSingle(String cid, String title, String content, String payload) {
        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数，requestid需要每次变化唯一
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        Settings settings = new Settings();
        pushDTO.setSettings(settings);

        //配置推送条件，1表示用户在线时使用个推通道，用户离线时使用厂商通道
        Strategy strategy = new Strategy();
        strategy.setDef(1);
        settings.setStrategy(strategy);
        //消息有效期，走厂商消息必须设置该值
        settings.setTtl(3600000);

        //在线走个推通道时推送的消息体
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        //此格式的透传消息做了特殊处理，会自动展示通知栏。开发者也可自定义其它格式，在客户端自己处理。
        pushMessage.setTransmission(" {title:\"" + title + "\",content:\"" + content + "\",payload:\"" + payload + "\"}");
        // 设置接收人信息
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.addCid(cid);

        //设置离线推送时的消息体
        PushChannel pushChannel = new PushChannel();
        //安卓离线厂商通道推送的消息体
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();

        ups.addOption("HW", "/message/android/notification/badge/class", "io.dcloud.PandoraEntry");
        ups.addOption("HW", "/message/android/notification/badge/add_num", 1);
        ups.addOption("HW", "/message/android/notification/importance", "HIGH");

        ThirdNotification thirdNotification = new ThirdNotification();
        ups.setNotification(thirdNotification);
        thirdNotification.setTitle(title);
        thirdNotification.setBody(content);
        thirdNotification.setClickType("intent");
        //注意：intent参数必须按下方要求的固定格式传值，intent错误会导致客户端无法收到消息
        thirdNotification.setIntent("intent://io.dcloud.unipush/?#Intent;scheme=unipush;launchFlags=0x4000000;component=io.dcloud.thwapp/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title=" + title + ";S.content=" + content + ";S.payload=" + payload + ";end");

        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);
        pushDTO.setPushChannel(pushChannel);

        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) {
            // success
            System.out.println(apiResult.getData());
        } else {
            // failed
            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }

    // 使用cid向特定的多个用户推送消息，title为通知栏消息的标题，content为通知栏消息的内容，payload为任何额外需要的信息，
    // 比如点击推送消息后APP要跳转到的界面，界面的参数等等
    public void pushToMany(List<String> cids, String title, String content, String payload) {
        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数，requestid需要每次变化唯一
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        Settings settings = new Settings();
        pushDTO.setSettings(settings);

        //配置推送条件，1表示用户在线时使用个推通道，用户离线时使用厂商通道
        Strategy strategy = new Strategy();
        strategy.setDef(1);
        settings.setStrategy(strategy);
        //消息有效期，走厂商消息必须设置该值
        settings.setTtl(3600000);

        //在线走个推通道时推送的消息体
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        //此格式的透传消息做了特殊处理，会自动展示通知栏。开发者也可自定义其它格式，在客户端自己处理。
        pushMessage.setTransmission(" {title:\"" + title + "\",content:\"" + content + "\",payload:\"" + payload + "\"}");

        //设置离线推送时的消息体
        PushChannel pushChannel = new PushChannel();
        //安卓离线厂商通道推送的消息体
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();

        ups.addOption("HW", "/message/android/notification/badge/class", "io.dcloud.PandoraEntry");
        ups.addOption("HW", "/message/android/notification/badge/add_num", 1);
        ups.addOption("HW", "/message/android/notification/importance", "HIGH");

        ThirdNotification thirdNotification = new ThirdNotification();
        ups.setNotification(thirdNotification);
        thirdNotification.setTitle(title);
        thirdNotification.setBody(content);
        thirdNotification.setClickType("intent");
        //注意：intent参数必须按下方要求的固定格式传值，intent错误会导致客户端无法收到消息
        thirdNotification.setIntent("intent://io.dcloud.unipush/?#Intent;scheme=unipush;launchFlags=0x4000000;component=io.dcloud.thwapp/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title=" + title + ";S.content=" + content + ";S.payload=" + payload + ";end");

        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);
        pushDTO.setPushChannel(pushChannel);

        AudienceDTO audienceDTO = new AudienceDTO();
        ApiResult<TaskIdDTO> createApiResult = pushApi.createMsg(pushDTO);
        if(!createApiResult.isSuccess()) {
            System.out.println("创建消息失败");
            return;
        }

        // 设置接收人信息
        Audience audience = new Audience();
        pushDTO.setAudience(audience);
        audience.setCid(cids);

        audienceDTO.setAudience(audience);
        audienceDTO.setTaskid(createApiResult.getData().getTaskId());
        audienceDTO.setAsync(true);

        ApiResult<Map<String, Map<String, String>>>  apiResult = pushApi.pushListByCid(audienceDTO);
        if(apiResult.isSuccess()) {
            System.out.println(apiResult.getData());
        } else {
            System.out.println("code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }
}
