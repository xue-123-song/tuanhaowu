import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import GroupInfoView from "../views/groupInfoView";

jest.mock("../utils/ajax");
const doJSONPost = require("../utils/ajax").doJSONPost;
const doGet = require("../utils/ajax").doGet;

jest.mock('react-router-dom', () => {
    return {
        ...jest.requireActual('react-router-dom'),
        useLocation: jest.fn(() => { return { state: { groupId: "123", back: "/" } } })
    }
})

describe("follow group", () => {
    test("follow group", async() => {
        doGet.mockResolvedValueOnce({
            "groupId": "123",
            "groupLeader": "团长",
            "groupTitle": "标题",
            "groupDescription": "描述",
            "startTime": "2022-09-01 00:00:00",
            "endTime": "2022-11-01 00:00:00",
            "logistics": 1,
            "items": [{
                "itemTitle": "苹果",
                "itemPrice": 999,
                "itemStock": 2,
                "itemImage": ""
            }],
        })
        await waitFor(() => render(<Router><GroupInfoView user={{userid: "12345"}} /></Router>));

        await screen.findByText("团长");
        await screen.findByText("标题");
        await screen.findByText("描述");
        await screen.findByText("同城配送");
        await screen.findByText("正在热卖中");

        await screen.findByText("苹果");
        await screen.findByText("￥9.99");
        await screen.findByText("剩余 2 件");

        let elem = screen.getByText('前往购买');
        act(() => userEvent.click(elem));

        await waitFor(() => {
            expect(location.pathname).toBe('/goods_view');
        })
    })
})
