import { encrypt, decrypt } from './encrypt';

export const ip = "http://124.71.160.191:8080"
//export const ip = "http://10.144.100.2:8080";
//export const ip = "http://localhost:8080";
//export const ip = "http://192.168.1.107:8080";
//export const ip = "http://192.168.10.101:8080";
//export const ip = "http://192.168.3.148:8080";

export const doFilePost = async(url, file) => {
    let formData = new FormData();
    formData.append("file", file);

    let opts = {
        method: "POST",
        body: formData,
        credentials: "include"
    };

    let response = await fetch(ip + url, opts);
    let imageURL = await response.text();
    if (imageURL !== "") imageURL = ip + "/tourist" + imageURL;
    return imageURL;
}

export const doJSONPost = async(url, json) => {
    let opts = {
        method: "POST",
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
            'Authorization': decrypt(localStorage.getItem('thwJwtToken')),
        },
        body: JSON.stringify(json),
        credentials: "include"
    }

    let response = await fetch(ip + url, opts);
    let responseJSON = await response.json();
    let token = response.headers.get('Authorization');
    if (token) localStorage.setItem('thwJwtToken', encrypt(token));

    return responseJSON;
}

export const doJSONPut = async(url, json) => {
    let opts = {
        method: "PUT",
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(json),
        credentials: "include"
    }

    let response = await fetch(ip + url, opts);
    let responseJSON = await response.json();
    return responseJSON;
}

export const doGet = async(url) => {
    let opts = {
        method: "GET",
        headers: {
            Accept: 'application/json',
            'Authorization': decrypt(localStorage.getItem('thwJwtToken')),
        },
        credentials: "include"
    }

    let response = await fetch(ip + url, opts);
    let responseJSON = await response.json();
    return responseJSON;
}
