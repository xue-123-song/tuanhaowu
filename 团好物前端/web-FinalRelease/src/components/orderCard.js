import React, { useState } from 'react';
import { Avatar, List, Tag, Image, Space, Toast, Dialog, Button } from 'antd-mobile';
import { DeleteOutline, DownOutline, MessageOutline } from 'antd-mobile-icons';
import { IntToPrice } from '../utils/transformPrice';
import { doGet } from '../utils/ajax';
import { useNavigate } from 'react-router-dom';

export default (props) => {
    const orderStatuses = ['未支付', '待发货', '待收货', '已收货', '已取消']
    const orderTagColor = ["default", "primary", "warning", "success", "danger"];
    const navigate = useNavigate();
    const [showAll, setShowAll] = useState(false);

    const sum = (itemList) => {
        let num = 0;
        for(let i = 0; i < itemList.length; ++i) {
            const itemPrice = itemList[i].itemPrice;
            const itemBuynum = itemList[i].itemBuynum;
            console.log(itemPrice);
            num = Number(Number(num) + Number(itemPrice) * Number(itemBuynum));
        }
        return num;
    }

    const cancelOrder = () => {
        Dialog.confirm({
            content: '您确定要取消该订单吗？',
            confirmText: <div style={{color: 'red', fontWeight: 'bold'}}>确定</div>,
            onConfirm: async() => {
                let response = await doGet("/cancelOrder/" + props.info.orderId);
                console.log(JSON.stringify(response));
                if(response.success == 1) {
                    let url = "/getOrdersForGroupLeader/" + props.userid;
                    let orderInfo = await doGet(url);
                    props.setGroupOrderList(orderInfo);
                    console.log(JSON.stringify(orderInfo));
                    Toast.show(response.meg);
                } else {
                    Toast.show(response.meg);
                }
            },
        })
    }

    return (
        <div style={{width: '94%', marginLeft: '3%'}}>
            <List.Item title={
                <span style={{ height: '28px', display: 'flex', alignItems: 'center', paddingRight: '3%' }} >
                    <Avatar src={props.info.picture} style={{'--size': '20px', marginRight: '5px', marginLeft: '5px'}} />
                    <span style={{ fontSize: 14, fontWeight: 'bold', color: 'black' }}>{ props.info.name }</span>
                    <Tag style={{marginLeft: '10px'}} color={orderTagColor[props.info.orderStatus]}>{orderStatuses[props.info.orderStatus]}</Tag>
                    <span style={{ marginLeft: 'auto' }}>订单号: { props.info.orderId }</span>
                </span>
            } style={{ borderRadius: 5, marginTop: 0 }}>
                <List style={{ '--border-bottom': 0 }}>
                    {
                        (props.info.orderitemList.length < 1 || showAll) ? props.info.orderitemList.map((item, index) => {
                            return <OrderItem item={item} />
                        }) : 
                        <>
                            <OrderItem item={props.info.orderitemList[0]} />
                            <div style={{ 'paddingLeft': '16px', 'paddingRight': '16px' }} onClick={()=>setShowAll(true)}>
                                <span style={{ fontSize: 12, color: 'gray' }}>显示余下的{props.info.orderitemList.length - 1}件商品</span>
                                <span style={{ float: 'right' }}><DownOutline fontSize={12} color='gray' /></span>
                            </div>
                        </>
                    }
                    <List.Item extra={
                        <div>共计
                            <span style={{fontWeight: 'bold', fontSize: '14px', color: 'red'}}>￥{IntToPrice(sum(props.info.orderitemList))}</span>
                            , 实付
                            <span style={{fontWeight: 'bold', fontSize: '14px', color: 'green'}}>￥{IntToPrice(sum(props.info.orderitemList))}</span>
                        </div>
                    }>
                    </List.Item>
                </List>
                <div style={{ marginTop: '5px', marginRight: '5px' }} className="footer2">
                <Space direction='horizontal'>
                    <Button className='adm-button2' size='mini' fill='outline' color='success'><MessageOutline color='green' fontSize={14} /></Button>
                    <Button className='adm-button2' size='mini' fill='outline' color='primary' onClick={() => { navigate('/logistics_view', { state: { info: props.info } }) }}>查看物流</Button>
                    <Button className='adm-button2' size='mini' fill='outline' color='danger' onClick={() => cancelOrder()}>取消订单</Button>
                    <Button className='adm-button2' size='mini' fill='outline' color='default' disabled><DeleteOutline fontSize={14} /> </Button>
                </Space>
                </div>
            </List.Item>
        </div>
    );
}

const OrderItem = (props) => {
    const itemPrice = props.item.itemPrice;
    const itemBuynum = props.item.itemBuynum;

    return (
        <List.Item
        width='100%'
        style={{flex: 1}}
        prefix={<div className='imagesContainer'><Image src={props.item.itemImage} style={{ '--height': '48px', '--width': '48px' }} /></div>}
        description={<div><span style={{fontWeight: 'bold'}}>￥{IntToPrice(itemPrice)}</span> x {itemBuynum}</div>}
        extra={<div style={{width: '100%', fontSize: 15, fontWeight: 700, color: 'red'}}>￥{IntToPrice(Number(itemPrice) * itemBuynum)}</div>}
        >
            <span style={{ fontSize: 10, fontWeight: 'normal' }}>{props.item.itemName}</span>
        
        </List.Item>
    );
}