import { ToYYYYMMDD, ToDisplay, ToStandard } from "../utils/transformTime";

test("to yyyy-mm-dd hh:MM:ss", () => {
    expect(ToYYYYMMDD(new Date(2022, 0, 1, 0, 0, 0))).toBe('2022-01-01 00:00:00');
    expect(ToYYYYMMDD(new Date(2022, 9, 30, 23, 59, 59))).toBe('2022-10-30 23:59:59');
})

test("to display", () => {
    expect(ToDisplay(new Date(2022, 0, 1, 0, 0, 0))).toBe('2022年01月01日00:00:00');
    expect(ToDisplay(new Date(2022, 9, 30, 23, 59, 59))).toBe('2022年10月30日23:59:59');
})

test("to standard", () => {
    expect(ToStandard("2022-01-01 00:00:00")).toEqual(new Date(2022, 0, 1, 0, 0, 0));
    expect(ToStandard("2022-10-30 23:59:59")).toEqual(new Date(2022, 9, 30, 23, 59, 59));
})
