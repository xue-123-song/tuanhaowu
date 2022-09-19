import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import LoginView from '../views/loginView';
import PersonInfo from '../views/personInfo';
import RequireAuth from '../utils/requireAuth';
import RequireUnAuth from '../utils/requireUnAuth';

jest.mock("../services/userService");
let login = require("../services/userService").login;
let logout = require("../services/userService").logout;
let checkAuth = require("../services/userService").checkAuth;
jest.mock('../components/picAuthCode');
const PicAuthCode = require("../components/picAuthCode").default;

describe("login test", () => {
    let setRole, setUser, changeAnother;

    beforeEach(async () => {
        login.mockResolvedValue({ "userid": "12345", "username": "用户名" });
        PicAuthCode.mockImplementation((props) => <div id="another" onClick={() => { changeAnother(); props.setCode(); }}></div>)
        setRole = jest.fn((role) => { return role });
        setUser = jest.fn((user) => { return user });
        changeAnother = jest.fn();
        await waitFor(() => render(
            <Router>
                <LoginView setRole={setRole} setUser={setUser} />
            </Router>)
        );
    })

    test("correct login", async() => {
        let elem = screen.getByPlaceholderText('请输入用户名');
        act(() => userEvent.type(elem, "用户名") );
        elem = screen.getByPlaceholderText('请输入密码');
        act(() => userEvent.type(elem, "密码") );
        elem = screen.getByPlaceholderText('请输入验证码');
        act(() => userEvent.type(elem, "aaaa"));

        elem = screen.getByText("登录");
        act(() => userEvent.click(elem));

        await waitFor(() => {
            expect(login).toBeCalled();
            expect(setRole).toBeCalledTimes(1);
            expect(setUser).toBeCalledTimes(1);
            expect(setUser.mock.lastCall).toEqual([{ "userid": "12345", "username": "用户名" }]);
            expect(location.pathname).toBe("/");
        })
    })

    test("no login", async () => {
        let elem = screen.getByText("以游客身份使用");
        userEvent.click(elem);
        await waitFor(() => {
            expect(setRole).toBeCalled();
            expect(setRole.mock.calls).toEqual([["游客"]]);
            expect(location.pathname).toBe("/");
        })
    })

    test("wrong picAuth", async () => {
        let elem = screen.getByPlaceholderText('请输入用户名');
        act(() => userEvent.type(elem, "用户名") );
        elem = screen.getByPlaceholderText('请输入密码');
        act(() => userEvent.type(elem, "密码") );
        elem = screen.getByPlaceholderText('请输入验证码');
        act(() => userEvent.type(elem, "bbbb"));

        elem = screen.getByText("登录");
        act(() => userEvent.click(elem));

        await waitFor(async () => {
            await screen.findByText("验证码错误");
            expect(changeAnother).toBeCalled();
            expect(login).not.toBeCalled();
        })
    })

    test("go to register", () => {
        let elem = screen.getByText("新用户注册");
        userEvent.click(elem);
        expect(location.pathname).toBe("/register");
    })

    test("wrong login", async () => {
        login.mockResolvedValue(null);
        let elem = screen.getByPlaceholderText('请输入用户名');
        act(() => userEvent.type(elem, "用户名") );
        elem = screen.getByPlaceholderText('请输入密码');
        act(() => userEvent.type(elem, "密码") );
        elem = screen.getByPlaceholderText('请输入验证码');
        act(() => userEvent.type(elem, "aaaa"));

        elem = screen.getByText("登录");
        act(() => userEvent.click(elem));

        await waitFor(() => {
            expect(login).toBeCalled();
            expect(setRole).not.toBeCalled();
            expect(setUser).not.toBeCalled();
        })
    })
})

describe("logout", () => {
    test("logout", async() => {
        const mockSetRole = jest.fn();
        const mockSetUser = jest.fn();
        const userInfo = {
            "picture": "",
            "userid": "12345",
            "account": "账号名称",
            "name": "我的名字",
            "tel": "1234567",
            "address": "上海",
        }
        await waitFor(() => render(<Router><PersonInfo user={userInfo} setUser={mockSetUser} setRole={mockSetRole} /></Router>));
        screen.getByText("12345");
        screen.getByText("账号名称");
        screen.getAllByText("我的名字");
        screen.getByText("1234567");
        screen.getByText("上海");
        act(() => userEvent.click(screen.getByText("退出登录")));

        await waitFor(() => {
            expect(logout).toBeCalled();
            expect(mockSetUser).toBeCalled();
            expect(mockSetRole).toBeCalled();
            expect(location.pathname).toBe("/login");
        })
    })
})

describe("require auth", () => {
    test("require auth and is auth", async() => {
        checkAuth.mockReturnValue(true);
        await waitFor(() => render(<Router><RequireAuth><div>已登录</div></RequireAuth></Router>));
        expect(checkAuth).toBeCalled();
        await screen.findByText("已登录");
    })

    test("require auth and not auth", async() => {
        checkAuth.mockReturnValue(false);
        await waitFor(() => render(<Router><RequireUnAuth><div>未登录</div></RequireUnAuth></Router>));
        expect(checkAuth).toBeCalled();
        await screen.findByText("未登录");
    })
})
