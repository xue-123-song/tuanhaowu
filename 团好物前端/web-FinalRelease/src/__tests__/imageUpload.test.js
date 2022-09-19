import { imageUpload } from '../utils/imageUpload';

jest.mock('../utils/ajax');
const doFilePost = require('../utils/ajax').doFilePost;

test("image upload", async() => {
    doFilePost.mockResolvedValue("/picture/1.jpg");
    let url = await imageUpload(null);
    expect(doFilePost).toBeCalled();
    expect(url).toEqual({ url: "/picture/1.jpg" });
})

test("image upload fail", async() => {
    doFilePost.mockResolvedValue("");
    try {
        await imageUpload(null);
        expect(false).toBe(true);
    } catch (e) {
        expect(true).toBe(true);
    }
    expect(doFilePost).toBeCalled();
})
