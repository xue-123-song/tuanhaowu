import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter as Router } from 'react-router-dom';
import AddItemPopup from '../components/addItemPopup';

jest.mock('../utils/imageUpload');
const imageUpload = require('../utils/imageUpload').imageUpload;

const delay = ms => new Promise((resolve, _) => setTimeout(resolve, ms));

describe("AddItem", () => {
    test("AddItem success1", async () => {
        const mockSetVisible = jest.fn();
        const mockAddItem = jest.fn();
        const { container } = render(
            <Router>
                <AddItemPopup visible={true} setVisible={mockSetVisible} onFinish={mockAddItem} append={false} />
            </Router>
        );

        let elem = screen.getByPlaceholderText('请输入商品名称');
        act(() => { userEvent.type(elem, "商品名称") });
        elem = screen.getByDisplayValue("0");
        act(() => { userEvent.type(elem, "10") });
        elem = screen.getByPlaceholderText('请输入商品价格');
        act(() => { userEvent.type(elem, "9.99") });

        imageUpload.mockResolvedValue("");

        elem = screen.getByText('添加');
        act(() => { userEvent.click(elem) });

        await waitFor(() => {
            expect(mockSetVisible).toBeCalledWith(false);
            expect(mockAddItem).toBeCalled();
        })
    })

    test("AddItem success2", async () => {
        const mockSetVisible = jest.fn();
        const mockAddItem = jest.fn();
        const { container } = render(
            <Router>
                <AddItemPopup visible={true} setVisible={mockSetVisible} onFinish={mockAddItem} append={false} />
            </Router>
        );

        let elem = screen.getByPlaceholderText('请输入商品名称');
        act(() => { userEvent.type(elem, "商品名称") });
        elem = screen.getByDisplayValue("0");
        act(() => { userEvent.type(elem, "10") });
        elem = screen.getByPlaceholderText('请输入商品价格');
        act(() => { userEvent.type(elem, "9.99") });

        elem = screen.getByRole('checkbox');
        act(() => { userEvent.click(elem) });
        elem = await screen.findByText("请选择秒杀开始时间");
        act(() => userEvent.click(elem));
        act(() => screen.getAllByText("确定")[0].click());
        await delay(1000);
        elem = await screen.findByText("请选择秒杀截止时间");
        act(() => userEvent.click(elem));
        act(() => screen.getAllByText("确定")[1].click());

        elem = screen.getByText('添加');
        act(() => { userEvent.click(elem) });

        await waitFor(() => {
            expect(mockSetVisible).toBeCalledWith(false);
            expect(mockAddItem).toBeCalled();
        })
    })

    test("AddItem success3", async () => {
        const mockSetVisible = jest.fn();
        const mockAddItem = jest.fn();
        const { container } = render(
            <Router>
                <AddItemPopup visible={true} setVisible={mockSetVisible} onFinish={mockAddItem} append={true} />
            </Router>
        );

        let elem = screen.getByPlaceholderText('请输入商品名称');
        act(() => { userEvent.type(elem, "商品名称") });
        elem = screen.getByDisplayValue("0");
        act(() => { userEvent.type(elem, "10") });
        elem = screen.getByPlaceholderText('请输入商品价格');
        act(() => { userEvent.type(elem, "9.99") });

        elem = screen.getByRole('checkbox');
        console.log(elem);
        act(() => { userEvent.click(elem) });
        elem = await screen.findByText("请选择秒杀开始时间");
        act(() => userEvent.click(elem));
        act(() => screen.getAllByText("确定")[0].click());
        await delay(1000);
        elem = await screen.findByText("请选择秒杀截止时间");
        act(() => userEvent.click(elem));
        act(() => screen.getAllByText("确定")[1].click());

        elem = screen.getByText('添加');
        act(() => { userEvent.click(elem) });

        await waitFor(() => {
            expect(mockSetVisible).toBeCalledWith(false);
            expect(mockAddItem).toBeCalled();
        })
    })

    test("AddItem success4", async () => {
        const mockSetVisible = jest.fn();
        const mockAddItem = jest.fn();
        const { container } = render(
            <Router>
                <AddItemPopup visible={true} setVisible={mockSetVisible} onFinish={mockAddItem} append={true} />
            </Router>
        );

        let elem = screen.getByPlaceholderText('请输入商品名称');
        act(() => { userEvent.type(elem, "商品名称") });
        elem = screen.getByDisplayValue("0");
        act(() => { userEvent.type(elem, "10") });
        elem = screen.getByPlaceholderText('请输入商品价格');
        act(() => { userEvent.type(elem, "9.99") });

        imageUpload.mockResolvedValue("");

        elem = screen.getByText('添加');
        act(() => { userEvent.click(elem) });

        await waitFor(() => {
            expect(mockSetVisible).toBeCalledWith(false);
            expect(mockAddItem).toBeCalled();
        })
    })
})
