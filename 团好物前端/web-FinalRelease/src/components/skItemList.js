import {Avatar, Button, Dialog, List, Space, Tag, Toast} from "antd-mobile";
import Countdown from "antd/es/statistic/Countdown";
import {IntToPrice} from "../utils/transformPrice";
import useForceUpdate from "antd/es/_util/hooks/useForceUpdate";
import React, {useState} from "react";
import {doJSONPost} from "../utils/ajax";
import {decrypt} from "../utils/encrypt";

export default (props) =>{
    let item = props.item;
    let now = new Date();
    let start = new Date(item.skStartTime)
    let end = new Date(item.skEndTime)

    const [onSale, setOnSale] = useState(
        now.getTime() < start.getTime()  ? 0 :
            now.getTime() < end.getTime() ? 1 : 2
    );

    if( onSale === 1 )
        return (
            <>
                <List.Item
                    prefix={<Avatar src={item.itemImage} fit='contain' />}
                    onClick={()=>{
                        props.setPopInfo(item)
                        props.setVisible(true)
                    }}
                >
                    <Space direction={"vertical"}>
                        <Space style={{fontSize:16, color:"black"}}>
                            {item.itemName}
                            <Tag color='danger'>秒杀商品</Tag>
                        </Space>
                        <Space style={{fontSize:14, color:"grey"}}>
                            据秒杀结束还有
                            <div style={{color:"red", fontSize:14}}>
                                <Countdown  value={new Date(item.skEndTime).getTime()} format="D 天 H 时 m 分 s 秒"
                                            valueStyle={{fontSize:14, fontWeight:"bold", color:"grey"}}
                                            onFinish={()=>{
                                                setOnSale(2)
                                            }}/>
                            </div>
                        </Space>
                    </Space>
                </List.Item>
                <List.Item extra={
                    <Space>
                        <div style={{fontSize:18, color:"black", fontWeight:"bold"}}>¥{IntToPrice(item.itemPrice)}</div>
                        <Button color={"primary"} size={"small"} onClick={async () => {
                            let request = {
                                groupId: props.item.belongGroupId,
                                userid: JSON.parse(decrypt(localStorage.getItem("user"))).userid,
                                tel:JSON.parse(decrypt(localStorage.getItem("user"))).tel,
                                address:JSON.parse(decrypt(localStorage.getItem("user"))).address,
                                itemId: props.item.itemId
                            }
                            let response = await doJSONPost('/secKill', request)
                            console.log(response)
                            if(response.isSuccess)
                                Toast.show({content: '恭喜你，抢到商品', position: 'bottom'})
                            else
                                Toast.show({content: '手慢了，商品已被抢光', position: 'bottom'})
                        }}>秒杀进行中</Button>
                    </Space>
                }>
                    单价
                </List.Item>
            </>
        )
    else if(onSale === 2)
        return (
            <>
                <List.Item
                    prefix={<Avatar src={item.itemImage} fit='contain' />}
                    onClick={()=>{
                        props.setPopInfo(item)
                        props.setVisible(true)
                    }} >
                    <Space style={{fontSize:16, color:"black"}}>
                        {item.itemName}
                        <Tag color='danger'>秒杀商品</Tag>
                    </Space>
                </List.Item>
                <List.Item extra={
                    <Space style={{fontSize:16, color:"grey"}}>
                        <div style={{fontSize:18, color:"black", fontWeight:"bold"}}>¥{IntToPrice(item.itemPrice)}</div>
                        <Button disabled color={"primary"} size={"small"}>秒杀已结束</Button>
                    </Space>
                }>
                    单价
                </List.Item>
            </>
        )
    else return (
            <>
                <List.Item
                    prefix={<Avatar src={item.itemImage} fit='contain' />}
                    onClick={()=>{
                        props.setPopInfo(item)
                        props.setVisible(true)
                    }} >
                    <Space direction={"vertical"}>
                        <Space style={{fontSize:16, color:"black"}}>
                            {item.itemName}
                            <Tag color='danger'>秒杀商品</Tag>
                        </Space>
                        <Space style={{fontSize:14, color:"grey"}}>
                            据秒杀开始还有
                            <Countdown  value={new Date(item.skStartTime).getTime()} format="D 天 H 时 m 分 s 秒"
                                        valueStyle={{fontSize:14, fontWeight:"bold", color:"grey"}}
                                        onFinish={()=>{
                                            setOnSale(1)
                                        }}/>
                        </Space>
                    </Space>
                </List.Item>
                <List.Item extra={
                    <Space>
                        <div style={{fontSize:18, color:"black", fontWeight:"bold"}}>¥{IntToPrice(item.itemPrice)}</div>
                        <Button disabled color={"primary"} size={"small"}>秒杀未开始</Button>
                    </Space>
                }>
                    单价
                </List.Item>
            </>
        )
}
