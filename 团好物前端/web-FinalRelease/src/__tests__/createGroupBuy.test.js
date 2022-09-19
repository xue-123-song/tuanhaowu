import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import CreateGroupBuy from '../views/createGroupBuy';

jest.mock('../services/groupService');
jest.mock('../utils/imageUpload');
const createGroupBuy = require('../services/groupService').createGroupBuy;
const imageUpload = require('../utils/imageUpload').imageUpload;

const groupInfo = {
    "title": "活动标题",
    "description": "活动内容",
}

const delay = ms => new Promise((resolve, _) => setTimeout(resolve, ms));

describe("Create GroupBuy", () => {
    test("Create GroupBuy", async () => {
        const { container } = render(<Router><CreateGroupBuy /></Router>);
        createGroupBuy.mockResolvedValue({"status": 0, "msg": "创建团购成功" });
        let elem = screen.getByPlaceholderText("请输入团购活动标题");
        act(() => userEvent.type(elem, groupInfo.title));
        elem = screen.getByPlaceholderText("请输入团购活动内容");
        act(() => userEvent.type(elem, groupInfo.description));
        elem = screen.getAllByText("请选择时间");
        act(() => userEvent.click(elem[0]));
        act(() => screen.getAllByText("确定")[0].click());
        await delay(1000);
        act(() => userEvent.click(elem[1]));
        act(() => screen.getAllByText("确定")[1].click());
        elem = screen.getByText("快递");
        act(() => userEvent.click(elem));

        elem = screen.getByText("提交");
        act(() => userEvent.click(elem));

        await waitFor(async() => {
            await screen.findAllByText("创建团购成功");
        })

        elem = container.querySelector(".adm-nav-bar-back-arrow");
        act(() => userEvent.click(elem));
    })
})
