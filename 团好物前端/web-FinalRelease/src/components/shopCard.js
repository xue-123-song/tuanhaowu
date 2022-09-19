import {Col,Card} from 'antd';
import {AutoCenter, Ellipsis, Image, List, Space, Swiper,} from "antd-mobile";
import {IntToPrice} from "../utils/transformPrice";
import {useNavigate} from "react-router-dom";
import React from "react";

const { Meta } = Card;

const ShopCard = (props) => {
    const navigate = useNavigate()

    if(props.groupInfo){
        if(props.groupInfo.items.length === 0) return
    const items = props.groupInfo.items.map((item, index)=>(
        <Swiper.Item key={index}>
            <Image src={item.itemImage} height={140} width={140} style={{borderRadius:8, margin:"auto"}}/>
            <div style={{height:5}}/>
            <AutoCenter><Space direction={"vertical"}>
                <div style={{fontSize:14}}>{item.itemName}</div>
                <div style={{fontWeight:"bold", fontSize:16, color:"red"}}>¥{IntToPrice(item.itemPrice)}</div>
            </Space></AutoCenter>
            <div style={{height:10}}/>
        </Swiper.Item>
    ))
    return (
            <Card bodyStyle={{ padding:4}} style={{borderRadius:12 ,margin:5}} onClick={()=>{navigate('/group_info', {state:{groupId:props.groupInfo.groupId ,back:'/', backState:null}})}}>
                <Ellipsis style={{fontWeight:"bold", fontFamily:"仿宋",fontSize:16, margin:5, wordBreak:"break-all"}} content={
                    props.groupInfo.groupTitle
                }/>
                <Swiper>{items}</Swiper>
            </Card>
    );
}
    else return (
        <Card bodyStyle={{ padding:4}} style={{borderRadius:12 ,margin:5}} onClick={()=>{navigate('/group_info', {state:{groupId:props.item.belongGroupId,back:'/', backState:null}})}}>
            <Image src={props.item.itemImage} height={140} width={140} style={{borderRadius:8, margin:"auto"}}/>
            <List>
                <List.Item>
                    <AutoCenter><Space direction={"vertical"}>
                        <div style={{fontSize:14}}>{props.item.itemName}</div>
                        <div style={{fontWeight:"bold", fontSize:16, color:"red"}}>¥{IntToPrice(props.item.itemPrice)}</div>
                    </Space></AutoCenter>
                </List.Item>
            </List>
        </Card>
    )
}

export default ShopCard;
