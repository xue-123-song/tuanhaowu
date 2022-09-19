import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import OrderView from "../views/orderView";

jest.mock("../utils/ajax");
const doJSONPost = require("../utils/ajax").doJSONPost;
const doGet = require("../utils/ajax").doGet;

describe("test order", () => {
    test("test order", async () => {
        const orderList = [{
            name: "团员",
            orderStatus: 1,
            orderId: "1234567",
            picture: "",
            orderitemList: [],
        }];

        doGet.mockResolvedValueOnce(orderList);

        const mockSetOrderList = jest.fn();

        await waitFor(() => render(
            <Router>
                <OrderView groupOrderList={null} setGroupOrderList={mockSetOrderList} user={{userid: "12345"}} />
            </Router>
        ))

        expect(mockSetOrderList).toBeCalled();
        expect(mockSetOrderList).toBeCalledWith(orderList);

        await waitFor(() => {
            screen.getByText("团员");
            screen.getByText("待发货");
            screen.getByText("1234567");
        })
    })

    test("cancel order success", async() => {
        const orderList = [{
            name: "团员",
            orderStatus: 3,
            orderId: "1234567",
            picture: "",
            orderitemList: [],
        }];

        const orderList2 = [{
            name: "团员",
            orderStatus: 4,
            orderId: "1234567",
            picture: "",
            orderitemList: [],
        }];

        doGet.mockResolvedValueOnce(orderList).mockResolvedValueOnce({
            success: 1,
            meg: "订单已取消",
        }).mockResolvedValueOnce(orderList2);

        const mockSetOrderList = jest.fn();

        await waitFor(() => render(
            <Router>
                <OrderView groupOrderList={null} setGroupOrderList={mockSetOrderList} user={{userid: "12345"}} />
            </Router>
        ))

        expect(mockSetOrderList).toBeCalled();
        expect(mockSetOrderList).toBeCalledWith(orderList);

        let elem = screen.getByText("取消订单");
        act(() => userEvent.click(elem));

        elem = screen.getByText("确定");
        act(() => userEvent.click(elem));

        await waitFor(() => {
            expect(doGet).toBeCalledTimes(3);
            expect(mockSetOrderList).toBeCalledWith(orderList2);

            screen.getByText("订单已取消");
            screen.getByText("已取消");
        })
    })

    test("cancel order fail", async() => {
        const orderList = [{
            name: "团员",
            orderStatus: 3,
            orderId: "1234567",
            picture: "",
            orderitemList: [],
        }];

        const orderList2 = [{
            name: "团员",
            orderStatus: 4,
            orderId: "1234567",
            picture: "",
            orderitemList: [],
        }];

        doGet.mockResolvedValueOnce(orderList).mockResolvedValueOnce({
            success: 0,
            meg: "取消订单失败",
        }).mockResolvedValueOnce(orderList2);

        const mockSetOrderList = jest.fn();

        await waitFor(() => render(
            <Router>
                <OrderView groupOrderList={null} setGroupOrderList={mockSetOrderList} user={{userid: "12345"}} />
            </Router>
        ))

        expect(mockSetOrderList).toBeCalled();
        expect(mockSetOrderList).toBeCalledWith(orderList);

        let elem = screen.getByText("取消订单");
        act(() => userEvent.click(elem));

        elem = screen.getByText("确定");
        act(() => userEvent.click(elem));

        await waitFor(() => {
            expect(doGet).toBeCalledTimes(2);
            expect(mockSetOrderList).not.toBeCalled();

            screen.getByText("取消订单失败");
            try {
                screen.getByText("已取消");
                expect(false).toBe(true);
            } catch(e) {
                expect(true).toBe(true);
            }
        })
    })
})

