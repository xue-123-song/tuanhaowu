import { screen } from '@testing-library/react';
import { cancelCreatedGroupBuy, createGroupBuy, deleteCreatedGroupBuy, finishGroupAdvance, getCreatedGroupBuy, modifyCreatedGroupBuy } from '../services/groupService';
import { encrypt } from '../utils/encrypt';
jest.mock('../utils/ajax');
const mockStorage = jest.spyOn(window.localStorage.__proto__, 'getItem');
const doJSONPost = require('../utils/ajax').doJSONPost;
const doGet = require('../utils/ajax').doGet;

describe("createNewGroup", () => {
    test("createNewGroup success1", async() => {
        const groupInfo = {
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: new Date(2022, 0, 1, 0, 0, 0),
            endTime: new Date(2022, 11, 1, 0, 0, 0),
            logistics: [1],
        }
        const items = [{
            itemName: '商品名称',
            itemStock: 10,
            itemPrice: 999,
            itemSeckill: false,
            itemImage: "",
        }]
        mockStorage.mockReturnValue(encrypt(JSON.stringify({ name: '名字' })));
        const message = { "status": 0, "msg": "创建团购成功" };
        doJSONPost.mockResolvedValue(message);
        let response = await createGroupBuy(groupInfo, items);
        expect(doJSONPost).toBeCalled();
        expect(response).toEqual(message);
        expect(doJSONPost.mock.lastCall[1]).toEqual({
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: "2022-01-01 00:00:00",
            endTime: "2022-12-01 00:00:00",
            logistics: 1,
            items: [{
                itemName: '商品名称',
                itemStock: 10,
                itemPrice: 999,
                itemSeckill: false,
                itemImage: "",
            }],
            groupLeader: '名字',
        })
    })

    test("createNewGroup success2", async() => {
        const groupInfo = {
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: new Date(2022, 0, 1, 0, 0, 0),
            endTime: new Date(2022, 11, 1, 0, 0, 0),
            logistics: [1],
        }
        const items = [{
            itemName: '商品名称',
            itemStock: 10,
            itemPrice: 999,
            itemSeckill: true,
            skStartTime: new Date(2022, 1, 1, 0, 0, 0),
            skEndTime: new Date(2022, 10, 1, 0, 0, 0),
            itemImage: "",
        }]
        mockStorage.mockReturnValue(encrypt(JSON.stringify({ name: '名字' })));
        const message = { "status": 0, "msg": "创建团购成功" };
        doJSONPost.mockResolvedValue(message);
        let response = await createGroupBuy(groupInfo, items);
        expect(doJSONPost).toBeCalled();
        expect(response).toEqual(message);
        expect(doJSONPost.mock.lastCall[1]).toEqual({
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: "2022-01-01 00:00:00",
            endTime: "2022-12-01 00:00:00",
            logistics: 1,
            items: [{
                itemName: '商品名称',
                itemStock: 10,
                itemPrice: 999,
                itemSeckill: true,
                skStartTime: "2022-02-01 00:00:00",
                skEndTime: "2022-11-01 00:00:00",
                itemImage: "",
            }],
            groupLeader: '名字',
        })
    })

    test("createNewGroup fail1", async() => {
        const groupInfo = {
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: new Date(2022, 0, 1, 0, 0, 0),
            endTime: new Date(2022, 11, 1, 0, 0, 0),
            logistics: [1],
        }
        const items = [{
            itemName: '商品名称',
            itemStock: 10,
            itemPrice: 999,
            itemSeckill: true,
            skStartTime: new Date(2021, 1, 1, 0, 0, 0),
            skEndTime: new Date(2022, 10, 1, 0, 0, 0),
            itemImage: "",
        }]
        mockStorage.mockReturnValue(encrypt(JSON.stringify({ name: '名字' })));
        const message = { "status": 0, "msg": "创建团购成功" };
        doJSONPost.mockResolvedValue(message);
        let response = await createGroupBuy(groupInfo, items);
        await screen.findByText("商品1的秒杀开始时间不能早于团购开始时间");
        expect(doJSONPost).not.toBeCalled();
        expect(response).toBeNull();
    })

    test("createNewGroup fail2", async() => {
        const groupInfo = {
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: new Date(2022, 0, 1, 0, 0, 0),
            endTime: new Date(2022, 11, 1, 0, 0, 0),
            logistics: [1],
        }
        const items = [{
            itemName: '商品名称',
            itemStock: 10,
            itemPrice: 999,
            itemSeckill: true,
            skStartTime: new Date(2022, 1, 1, 0, 0, 0),
            skEndTime: new Date(2023, 10, 1, 0, 0, 0),
            itemImage: "",
        }]
        mockStorage.mockReturnValue(encrypt(JSON.stringify({ name: '名字' })));
        const message = { "status": 0, "msg": "创建团购成功" };
        doJSONPost.mockResolvedValue(message);
        let response = await createGroupBuy(groupInfo, items);
        await screen.findByText("商品1的秒杀结束时间不能晚于团购结束时间");
        expect(doJSONPost).not.toBeCalled();
        expect(response).toBeNull();
    })
})

describe("getCreatedGroup", () => {
    test("getCreatedGroup", async() => {
        doGet.mockResolvedValue([{ groupId: "12345" }]);
        expect(await getCreatedGroupBuy("团长")).toEqual([{ groupId: "12345" }]);
    })
})

describe("modifyCreatedGroup", () => {
    test("modifyCreatedGroup", async() => {
        const groupInfo = {
            groupId: '12345',
            groupLeader: '团长',
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: new Date(2022, 0, 1, 0, 0, 0),
            endTime: new Date(2022, 11, 1, 0, 0, 0),
            logistics: [1],
        }
        const response = { key: { status: 0, msg: '修改成功' }, value: [groupInfo] };
        doJSONPost.mockResolvedValue(response);
        expect(await modifyCreatedGroupBuy(groupInfo)).toEqual(response);
    })
})

describe("cancelCreatedGroup", () => {
    test("cancelCreatedGroup", async() => {
        const response = { key: { status: 0, msg: '取消成功' }, value: [] };
        doGet.mockResolvedValueOnce(response.key).mockResolvedValueOnce(response.value);
        expect(await cancelCreatedGroupBuy("", "")).toEqual(response);
    })
})

describe("finishGroupAdvance", () => {
    test("finishGroupAdvance", async() => {
        const groupInfo = {
            groupId: '12345',
            groupLeader: '团长',
            groupTitle: "活动标题",
            groupDescription: "活动内容",
            startTime: "2022-01-01 00:00:00",
            endTime: "2022-12-01 00:00:00",
            logistics: 1,
        }
        doJSONPost.mockImplementationOnce((_, info) => { return { key: { status: 0, msg: '团购已结束' }, value: [info] } });
        let response = await finishGroupAdvance(groupInfo);
        expect(new Date(response.value[0].endTime) <= new Date()).toBe(true);
    })
})

describe("deleteCreatedGroupBuy", () => {
    test("deleteCreatedGroupBuy success", async() => {
        const response = { key: { status: 0, msg: '删除成功' }, value: [] };
        doGet.mockResolvedValueOnce(response.key).mockResolvedValueOnce(response.value);
        expect(await deleteCreatedGroupBuy("", "")).toEqual(response);
    })

    test("deleteCreatedGroupBuy fail", async() => {
        const response = { key: { status: -1, msg: '删除失败' }, value: null };
        doGet.mockResolvedValueOnce(response.key).mockResolvedValueOnce([]);
        expect(await deleteCreatedGroupBuy("", "")).toEqual(response);
    })
})
