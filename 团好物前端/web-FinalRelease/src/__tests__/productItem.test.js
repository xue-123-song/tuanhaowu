import React from 'react';
import { act, screen, render, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ProductItem from '../components/productItem';

const delay = ms => new Promise((resolve, _) => setTimeout(resolve, ms));

describe("test productItem", () => {
    test("test 1", async () => {
        const items = [{
            itemName: "苹果",
            itemPrice: 999,
            itemStock: 10,
            itemImage: [{url: ""}],
        }]
        const mockSetItems = jest.fn();
        await waitFor(() => render(<ProductItem index='0' setItems={mockSetItems} items={items} />));
        screen.getByText("苹果");
        screen.getByText("￥9.99");
        screen.getByText("库存10件");
    })

    test("test 2", async () => {
        const items = [{
            itemName: "苹果",
            itemPrice: 999,
            itemStock: 10,
            itemImage: [{url: ""}],
        }]
        const mockSetItems = jest.fn();
        let wrapper;
        await waitFor(() => wrapper = render(<ProductItem index='0' setItems={mockSetItems} items={items} />));
        let elem = screen.getByText("删除");
        act(() => userEvent.click(elem));
        expect(mockSetItems).toBeCalled();
        expect(mockSetItems).toBeCalledWith([]);
    })

    test("test 3", async () => {
        const items = [{
            itemName: "苹果",
            itemPrice: 999,
            itemStock: 10,
            itemImage: [{url: ""}],
        }]
        const mockSetItems = jest.fn();
        let wrapper;
        await waitFor(() => wrapper = render(<ProductItem index='0' setItems={mockSetItems} items={items} />));
        let elem = wrapper.container.querySelector(".imagesContainer");
        act(() => userEvent.click(elem));
        elem = screen.getByDisplayValue('苹果');
        act(() => { userEvent.clear(elem); userEvent.type(elem, "香蕉") });
        elem = screen.getByDisplayValue("10");
        act(() => { userEvent.clear(elem); userEvent.type(elem, "20") });
        elem = screen.getByDisplayValue("9.99");
        act(() => { userEvent.clear(elem); userEvent.type(elem, "99.99") });

        elem = screen.getByRole('checkbox');
        act(() => { userEvent.click(elem) });
        elem = await screen.findByText("请选择秒杀开始时间");
        act(() => userEvent.click(elem));
        act(() => screen.getAllByText("确定")[0].click());
        await delay(1000);
        elem = await screen.findByText("请选择秒杀截止时间");
        act(() => userEvent.click(elem));
        act(() => screen.getAllByText("确定")[1].click());

        elem = screen.getByText('修改');
        act(() => { userEvent.click(elem) });
        await waitFor(async () => {
            await screen.findByText("香蕉");
        })
    })
})
