import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import AdminGroupBuy from '../views/adminGroupBuy';
import { ToYYYYMMDD } from '../utils/transformTime';

jest.mock('../services/groupService');
const getCreatedGroupBuy = require('../services/groupService').getCreatedGroupBuy;
const modifyCreatedGroupBuy = require('../services/groupService').modifyCreatedGroupBuy;
const cancelCreatedGroupBuy = require('../services/groupService').cancelCreatedGroupBuy;
const finishGroupAdvance = require('../services/groupService').finishGroupAdvance;
const deleteCreatedGroupBuy = require('../services/groupService').deleteCreatedGroupBuy;
const delay = ms => new Promise((resolve, _) => setTimeout(resolve, ms));
const groupBuyList = [{
    "groupId": "1045",
    "groupLeader": "张三",
    "groupTitle": "果蔬生鲜",
    "groupDescription": "团购内容",
    "startTime": "2022-01-01 00:00:00",
    "endTime": "2022-01-06 00:00:00",
    "status": 1,
    "logistics": 1,
    "items": [],
    "orders": [],
}];

describe("Admin GroupBuy", () => {
    test("Admin GroupBuy", async () => {
        let wrapper;
        getCreatedGroupBuy.mockResolvedValue(groupBuyList);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => wrapper = render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        expect(mockSetGroupBuyList).toBeCalled();
        await screen.findByText("果蔬生鲜");

        try { await screen.findByText("复制链接"); expect(true).toBe(false);}
        catch(e) { expect(true).toBe(true); }

        let elem = wrapper.container.querySelector("#qr");
        act(() => userEvent.click(elem));
        await screen.findByText("复制链接");
    })

    test("No GroupBuy", async () => {
        getCreatedGroupBuy.mockResolvedValue([]);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        await screen.findByText("暂无数据");
    })

    test("Go to create", async () => {
        getCreatedGroupBuy.mockResolvedValue(groupBuyList);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        let elem = screen.getByText("新建团购");
        userEvent.click(elem);
        expect(location.pathname).toBe("/create");
    })

    test("Go to view", async () => {
        getCreatedGroupBuy.mockResolvedValue(groupBuyList);
        let mockSetGroupBuyList = jest.fn(), wrapper;
        await act(async() => {
            await waitFor(() => wrapper = render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        let elem = wrapper.container.querySelector("#imgs");
        userEvent.click(elem);
        expect(location.pathname).toBe("/view");
    })

    test("modifyGroupBuy", async () => {
        let wrapper;
        getCreatedGroupBuy.mockResolvedValue(groupBuyList);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => wrapper = render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        let elem = wrapper.container.querySelector(".ant-dropdown-trigger");
        act(() => userEvent.hover(elem));
        elem = await screen.findByText("修改团购");
        act(() => userEvent.click(elem));
        elem = screen.getAllByText("果蔬生鲜")[1];
        act(() => userEvent.type(elem, "新鲜水果"));
        act(() => userEvent.click(screen.getByText("2022-01-01 00:00:00")));
        act(() => userEvent.click(screen.getAllByText("确定")[0]));
        await delay(1000);
        act(() => userEvent.click(screen.getByText("2022-01-06 00:00:00")));
        act(() => userEvent.click(screen.getAllByText("确定")[1]));
        elem = screen.getByText("同城配送");
        act(() => userEvent.click(elem));

        modifyCreatedGroupBuy.mockResolvedValue({"key": {
            "status": 0, "msg": "修改成功",
        }, "value": [{
            "groupId": "1045",
            "groupLeader": "张三",
            "groupTitle": "新鲜水果",
            "groupDescription": "团购内容",
            "startTime": "2022-01-01 00:00:00",
            "endTime": "2022-01-06 00:00:00",
            "status": 1,
            "logistics": 2,
            "items": [],
            "orders": [],
        }]});

        elem = screen.getByText("修改");
        act(() => userEvent.click(elem));

        await waitFor(async() => {
            expect(modifyCreatedGroupBuy).toBeCalled();
            await screen.findByText("修改成功");
            expect(mockSetGroupBuyList).toBeCalled();
            await screen.findByText("新鲜水果");
        })
    })

    test("cancelGroupBuy", async() => {
        let wrapper;
        getCreatedGroupBuy.mockResolvedValue(groupBuyList);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => wrapper = render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        await screen.findByText("已结束");

        let elem = wrapper.container.querySelector(".ant-dropdown-trigger");
        act(() => userEvent.hover(elem));
        elem = await screen.findByText("取消团购");
        act(() => userEvent.click(elem));

        cancelCreatedGroupBuy.mockResolvedValue({"key": {
            "status": 0, "msg": "取消团购成功",
        }, "value": [{
            "groupId": "1045",
            "groupLeader": "张三",
            "groupTitle": "果蔬生鲜",
            "groupDescription": "团购内容",
            "startTime": "2022-01-01 00:00:00",
            "endTime": "2022-01-06 00:00:00",
            "status": 0,
            "logistics": 1,
            "items": [],
            "orders": [],
        }]});

        await screen.findByText('您确定要取消 果蔬生鲜 吗?');
        elem = screen.getByText("确定");
        act(() => userEvent.click(elem));

        expect(cancelCreatedGroupBuy).toBeCalled();
        expect(mockSetGroupBuyList).toBeCalled();
        await screen.findByText("已取消");
    })

    test("deleteGroupBuy", async() => {
        let wrapper;
        getCreatedGroupBuy.mockResolvedValue(groupBuyList);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => wrapper = render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        await screen.findByText("已结束");

        deleteCreatedGroupBuy.mockResolvedValue({"key": {
            "status": 0, "msg": "删除成功",
        }, "value": []});

        let elem = wrapper.container.querySelector(".ant-dropdown-trigger");
        act(() => userEvent.click(elem));
        elem = await screen.findByText("删除记录");
        act(() => userEvent.click(elem));
        elem = screen.getAllByText("确定")[1];
        act(() => userEvent.click(elem));

        expect(deleteCreatedGroupBuy).toBeCalled();
        expect(mockSetGroupBuyList).toBeCalled();
        await screen.findByText("暂无数据");
    })

    test("finishGroup", async () => {
        let wrapper;
        let _groupBuyList = groupBuyList;
        _groupBuyList[0]["endTime"] = "2050-01-06 00:00:00";
        getCreatedGroupBuy.mockResolvedValue(_groupBuyList);
        let mockSetGroupBuyList = jest.fn();
        await act(async() => {
            await waitFor(() => wrapper = render(<Router><AdminGroupBuy user={{"name": "张三"}} groupBuyList={null} setGroupBuyList={mockSetGroupBuyList} /></Router>));
        })
        await screen.findByText("进行中");

        finishGroupAdvance.mockResolvedValue({"key": {
            "status": 0, "msg": "团购已结束",
        }, "value": [{
            "groupId": "1045",
            "groupLeader": "张三",
            "groupTitle": "果蔬生鲜",
            "groupDescription": "团购内容",
            "startTime": "2022-01-01 00:00:00",
            "endTime": ToYYYYMMDD(new Date()),
            "status": 1,
            "logistics": 1,
            "items": [],
            "orders": [],
        }]});

        let elem = wrapper.container.querySelector(".ant-dropdown-trigger");
        act(() => userEvent.hover(elem));
        elem = await screen.findByText("提前结束");
        act(() => userEvent.click(elem));

        expect(finishGroupAdvance).toBeCalled();
        expect(mockSetGroupBuyList).toBeCalled();
        await screen.findByText("已结束");
    })
})
