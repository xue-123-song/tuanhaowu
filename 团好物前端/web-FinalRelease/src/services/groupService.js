import { decrypt } from '../utils/encrypt';
import { Toast } from 'antd-mobile';
import { ToYYYYMMDD } from '../utils/transformTime';
import { doJSONPost, doGet } from '../utils/ajax';

const createGroupBuy = async (groupInfo, items) => {
    for(let i = 0; i < items.length; ++i) {
        if(!items[i].itemSeckill)   continue;
        if(items[i].skStartTime < groupInfo.startTime) {
            Toast.show('商品' + (Number(i) + 1) + '的秒杀开始时间不能早于团购开始时间' );
            return null;
        }
        if(items[i].skEndTime > groupInfo.endTime) {
            Toast.show('商品' + (Number(i) + 1) + '的秒杀结束时间不能晚于团购结束时间' );
            return null;
        }
    }

    for(let i = 0; i < items.length; ++i) {
        if(!items[i].itemSeckill)   continue;
        items[i]["skStartTime"] = ToYYYYMMDD(items[i].skStartTime);
        items[i]["skEndTime"] = ToYYYYMMDD(items[i].skEndTime);
    }
    groupInfo["items"] = items;
    let [logisticWay] = groupInfo.logistics;
    groupInfo["logistics"] = logisticWay;
    groupInfo["startTime"] = ToYYYYMMDD(groupInfo.startTime);
    groupInfo["endTime"] = ToYYYYMMDD(groupInfo.endTime);
    groupInfo["groupLeader"] = JSON.parse(decrypt(localStorage.getItem("user"))).name;

    let responseJSON = await doJSONPost("/createGroup", groupInfo);
    return responseJSON;
}

const getCreatedGroupBuy = async (groupLeader) => {
    let responseJSON = await doGet('/searchGroupByGroupLeader/' + groupLeader);
    return responseJSON;
}

const modifyCreatedGroupBuy = async (groupInfo) => {
    let [logisticWay] = groupInfo.logistics;
    groupInfo["logistics"] = logisticWay;
    groupInfo["startTime"] = ToYYYYMMDD(groupInfo.startTime);
    groupInfo["endTime"] = ToYYYYMMDD(groupInfo.endTime);
    let responseJSON = await doJSONPost("/changeGroupInfo", groupInfo);
    return responseJSON;
}

const cancelCreatedGroupBuy = async (groupId, groupLeader) => {
    let response = await doGet("/cancelGroup/" + groupId);
    let groupInfo = await doGet('/searchGroupByGroupLeader/' + groupLeader);
    return { "key": response, "value": groupInfo };
}

const finishGroupAdvance = async (groupInfo) => {
    let info = groupInfo;
    info["endTime"] = ToYYYYMMDD(new Date());
    let response = await doJSONPost("/changeGroupInfo", info);
    return response;
}

const deleteCreatedGroupBuy = async (groupId, groupLeader) => {
    let response = await doGet("/deleteGroupByID/" + groupId), groupInfo = null;
    if(response.status == 0)
        groupInfo = await doGet('/searchGroupByGroupLeader/' + groupLeader);
    return { "key": response, "value": groupInfo };
}

export { createGroupBuy, getCreatedGroupBuy, modifyCreatedGroupBuy, cancelCreatedGroupBuy, finishGroupAdvance, deleteCreatedGroupBuy };