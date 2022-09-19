import { doFilePost, doJSONPost, doGet, ip } from "../utils/ajax";
import { decrypt, encrypt } from "../utils/encrypt";

const token = "123456789101112131415161718192021222324252627282930";
const mockStorage = jest.spyOn(window.localStorage.__proto__, 'getItem');
const mockSetStorage = jest.spyOn(window.localStorage.__proto__, 'setItem');

describe("ajax", () => {
    test("doFilePost", async() => {
        global.fetch = jest.fn().mockImplementationOnce((url, opt) => {
            return new Response("/picture/1.jpg", {
                status: 200,
                statusText: 'OK',
                headers: {}
            })
        });
        let response = await doFilePost("", null);
        expect(fetch).toBeCalled();
        expect(response).toMatch(ip + "/picture/1.jpg");
    })

    test("doJSONPost", async() => {
        global.fetch = jest.fn().mockImplementationOnce((_, opts) => {
            if (opts.headers.Authorization !== token) return new Response(JSON.stringify({}), {
                status: 403,
                statusText: 'Forbidden',
                headers: {}
            })
            return new Response(JSON.stringify({ userid: "12345" }), {
                status: 200,
                statusText: 'OK',
                headers: {}
            })
        });
        mockStorage.mockReturnValue(encrypt(token));
        let response = await doJSONPost("", {});
        expect(fetch).toBeCalled();
        expect(mockStorage).toBeCalled();
        expect(response).toEqual({ userid: "12345" });
    })

    test("doGet", async() => {
        global.fetch = jest.fn().mockImplementationOnce((_, opts) => {
            if (opts.headers.Authorization !== token) return new Response(JSON.stringify({}), {
                status: 403,
                statusText: 'Forbidden',
                headers: {}
            })
            return new Response(JSON.stringify({ userid: "12345" }), {
                status: 200,
                statusText: 'OK',
                headers: {}
            })
        });
        mockStorage.mockReturnValue(encrypt(token));
        let response = await doGet("");
        expect(fetch).toBeCalled();
        expect(mockStorage).toBeCalled();
        expect(response).toEqual({ userid: "12345" });
    })

    test("login", async() => {
        global.fetch = jest.fn().mockImplementationOnce((url, opts) => {
            if (opts.headers.Authorization !== token) return new Response(JSON.stringify({}), {
                status: 403,
                statusText: 'Forbidden',
                headers: { Authorization: (url.indexOf('/loginCheck') != -1 ? token : null) }
            })
            return new Response(JSON.stringify({ userid: "12345" }), {
                status: 200,
                statusText: 'OK',
                headers: {}
            })
        });
        mockStorage.mockReturnValue(null);
        await doJSONPost("/loginCheck", {});
        expect(fetch).toBeCalled();
        expect(mockSetStorage).toBeCalled();
        expect(decrypt(mockSetStorage.mock.lastCall[1])).toEqual(token);
    })

    test("fail upload", async() => {
        global.fetch = jest.fn().mockImplementationOnce((url, opt) => {
            return new Response("", {
                status: 200,
                statusText: 'OK',
                headers: {}
            })
        });
        let response = await doFilePost("", null);
        expect(fetch).toBeCalled();
        expect(response).toMatch("");
    })
})
