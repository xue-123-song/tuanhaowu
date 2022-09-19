export function ToYYYYMMDD(prop) {
    let time = new Date(prop);
    let newTime = time.getFullYear() + '-' + ('0' + (time.getMonth() + 1)).slice(-2) + '-' + ('0' + time.getDate()).slice(-2) + ' ';
    newTime = newTime + ('0' + time.getHours()).slice(-2) + ':' + ('0' + time.getMinutes()).slice(-2) + ':' + ('0' + time.getSeconds()).slice(-2);
    return newTime;
}

export function ToDisplay(prop) {
    let time = new Date(prop);
    let newTime = time.getFullYear() + '年' + ('0' + (time.getMonth() + 1)).slice(-2) + '月' + ('0' + time.getDate()).slice(-2) + '日';
    newTime = newTime + ('0' + time.getHours()).slice(-2) + ':' + ('0' + time.getMinutes()).slice(-2) + ':' + ('0' + time.getSeconds()).slice(-2);
    return newTime;
}

export function ToStandard(prop) {
    return new Date(prop);
}