import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import PersonCenter from '../views/personCenter';

jest.mock("../services/userService");
const queryInfo = require("../services/userService").queryInfo;

const userInfo = {
    "userid": "12345",
    "name": "用户名",
    "picture": "",
    "money": 1234,
}

describe("person center", () => {
    test("no login", async() => {
        const mockSetUser = jest.fn();
        await waitFor(() => render(<Router><PersonCenter role="游客" user={null} setUser={mockSetUser} /></Router>))
        let elem = screen.getByText("未登录");
        act(() => userEvent.click(elem));
        expect(location.pathname).toBe("/login");
    })

    test("no login2", async() => {
        const mockSetUser = jest.fn();
        await waitFor(() => render(<Router><PersonCenter role="游客" user={null} setUser={mockSetUser} /></Router>))
        let elem = screen.getByText("扫一扫");
        act(() => userEvent.click(elem));
        expect(location.pathname).toBe("/scan");
    })

    test("login", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团长" user={userInfo} setUser={mockSetUser} /></Router>))

        await waitFor(async() => {
            expect(queryInfo).toBeCalled();
            expect(mockStorage).toBeCalled();
            expect(mockSetUser).toBeCalled();
            await screen.findByText("用户名");
        })
    })

    test("login1", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团长" user={userInfo} setUser={mockSetUser} /></Router>))

        act(() => userEvent.click(screen.getByText("用户名")));
        expect(location.pathname).toBe('/info');
    })

    test("login2", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团长" user={userInfo} setUser={mockSetUser} /></Router>))

        act(() => userEvent.click(screen.getByText("扫一扫")));
        expect(location.pathname).toBe('/scan');
    })

    test("login3", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团长" user={userInfo} setUser={mockSetUser} /></Router>))

        act(() => userEvent.click(screen.getByText("切换身份")));
        expect(location.pathname).toBe('/switch');
    })

    test("login4", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团长" user={userInfo} setUser={mockSetUser} /></Router>))

        act(() => userEvent.click(screen.getByText("我的消息")));
        expect(location.pathname).toBe('/message');
    })

    test("login5", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团员" user={userInfo} setUser={mockSetUser} /></Router>))

        act(() => userEvent.click(screen.getByText("切换身份")));
        expect(location.pathname).toBe('/switch');
    })

    test("login6", async() => {
        const mockSetUser = jest.fn();
        queryInfo.mockResolvedValue(userInfo);
        const mockStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
        await waitFor(() => render(<Router><PersonCenter role="团员" user={userInfo} setUser={mockSetUser} /></Router>))

        act(() => userEvent.click(screen.getByText("扫一扫")));
        expect(location.pathname).toBe('/scan');
    })
})
