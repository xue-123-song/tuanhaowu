import React from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/header';
import { List, Avatar, Button, Space } from 'antd-mobile';
import { RightOutline } from 'antd-mobile-icons';
import { logout } from '../services/userService';

export default (props) => {
    const navigate = useNavigate();
    const text = (txt) => {
        return <div style={{marginTop: '12px'}}>{txt}</div>;
    }

    const handleLogout = () => {
        logout();
        props.setUser(null);
        props.setRole("游客");
        navigate("/login", { replace: true })
    }

    return (
        <div style={{flex: 1}}>
            <Space block direction='vertical'>
                <Header title='用户信息' onBack={() => { window.history.back(); }} />
                <List>
                    <List.Item prefix="头像" extra={<Avatar src={props.user.picture} />} />
                    <List.Item prefix={text("用户ID")} extra={text(props.user.userid)} style={{height: '50px'}} />
                    <List.Item prefix={text("用户名")} extra={text(props.user.account)} style={{height: '50px'}} />
                    <List.Item prefix={text("昵称")} extra={text(props.user.name)} style={{height: '50px'}} />
                    <List.Item prefix={text("默认收件人")} extra={text(props.user.name)} style={{height: '50px'}} />
                    <List.Item prefix={text("默认电话")} extra={text(props.user.tel)} style={{height: '50px'}} />
                    <List.Item prefix={text("默认地址")} extra={text(props.user.address)} />
                    <List.Item prefix={text("修改密码")} arrow={text(<RightOutline />)} onClick={()=>{}} style={{height: '50px'}} />
                </List>
                <div style={{width: '90%', marginLeft: '5%'}}>
                    <Button color='primary' size='large' block>修改信息</Button>
                    <div style={{height: '10px'}}></div>
                    <Button color='danger' onClick={() => {handleLogout()}} size='large' block>退出登录</Button>
                </div>
            </Space>
        </div>
    );
}