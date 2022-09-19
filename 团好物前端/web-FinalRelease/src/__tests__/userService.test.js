import { screen, waitFor } from '@testing-library/react';
import { checkAuth, login, logout, queryInfo } from '../services/userService';
import { encrypt } from '../utils/encrypt';
jest.mock('../utils/ajax');
const mockGetStorage = jest.spyOn(window.localStorage.__proto__, 'getItem');
const mockSetStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');
const mockRemoveStorage = jest.spyOn(window.localStorage.__proto__, 'removeItem');
const doJSONPost = require('../utils/ajax').doJSONPost;
const doGet = require('../utils/ajax').doGet;

describe("userService login", () => {
    test("login success", async() => {
        const user = { "userid": "12345" };
        doJSONPost.mockResolvedValueOnce(user);
        expect(await login("用户名", "密码")).toEqual(user);
        expect(mockSetStorage).toBeCalledTimes(2);
    })

    test("login fail1", async() => {
        const user = { "userid": "12345" };
        doJSONPost.mockResolvedValueOnce(user);
        let response = await login("用户名", "");
        await screen.findByText("密码不能为空");
        expect(response).toEqual(null);
        expect(doJSONPost).not.toBeCalled();
    })

    test("login fail2", async() => {
        const user = { "userid": "12345" };
        doJSONPost.mockResolvedValueOnce(user);
        let response = await login("", "密码");
        await screen.findByText("用户名不能为空");
        expect(response).toEqual(null);
        expect(doJSONPost).not.toBeCalled();
    })

    test("login fail3", async() => {
        const user = { "status": "404", "statusText": "Not Found" };
        doJSONPost.mockResolvedValueOnce(user);
        let response = await login("用户名", "密码");
        expect(response).toEqual(null);
        expect(doJSONPost).toBeCalled();
        expect(mockSetStorage).not.toBeCalled();
    })

    test("logout", async() => {
        logout();
        await waitFor(() => expect(mockRemoveStorage).toBeCalledTimes(3));
    })

    test("checkAuth", async() => {
        mockGetStorage.mockReturnValueOnce(encrypt("1234567")).mockReturnValueOnce(null);
        expect(checkAuth()).toBe(true);
        expect(checkAuth()).toBe(false);
        expect(mockRemoveStorage).toBeCalledTimes(3);
    })

    test("queryInfo", async() => {
        doGet.mockResolvedValueOnce({ userid: "12345" });
        expect(await queryInfo("12345")).toEqual({ userid: "12345" });
    })
})
