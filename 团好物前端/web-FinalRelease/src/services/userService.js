import { Toast } from "antd-mobile";
import { doJSONPost, doGet } from "../utils/ajax";
import { encrypt } from "../utils/encrypt";

const login = async(account, password) => {
    if (account === '' || account === null) {
        Toast.show("用户名不能为空");
        return null;
    }
    if (password === '' || password === null) {
        Toast.show("密码不能为空");
        return null;
    }
    let info = { "account": account + '&0', "password": password };
    let user = await doJSONPost("/loginCheck", info);
    if ("userid" in user) {
        localStorage.setItem("user", encrypt(JSON.stringify(user)));
        localStorage.setItem("role", '团长');
        return user;
    }
    return null;
}

const logout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("role");
    localStorage.removeItem("thwJwtToken");
}

const checkAuth = () => {
    let data = localStorage.getItem('thwJwtToken');
    if (data) {
        return true;
    } else {
        localStorage.removeItem('user');
        localStorage.removeItem('role');
        localStorage.removeItem('thwJwtToken');
    }
    return false;
};

const queryInfo = async (userid) => {
    let userInfo = await doGet("/queryUserInfo/" + userid);
    return userInfo;
}

export { login, logout, checkAuth, queryInfo };
