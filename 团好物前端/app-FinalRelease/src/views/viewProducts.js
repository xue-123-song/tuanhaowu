import React, { useState, useEffect } from 'react';
import Header from '../components/header';
import LoadingMask from '../components/loadingMask';
import { useLocation, useNavigate } from 'react-router-dom';
import { AddOutline } from 'antd-mobile-icons';
import AddItemPopup from '../components/addItemPopup';
import ProductList from '../components/productList';
import GroupStatistic from '../components/groupStatistic';
import { doJSONPost, doGet } from '../utils/ajax';
import { ToStandard, ToYYYYMMDD } from '../utils/transformTime';
import { Mask, Toast, SpinLoading, List, Tag, Swiper, Image, Ellipsis, Empty } from 'antd-mobile';
import { FileWrongOutline } from 'antd-mobile-icons';
import OrderCard from '../components/orderCard';
import { PriceToInt } from '../utils/transformPrice';
import { Tabs } from 'antd-mobile/es/components/tabs/tabs';
import Countdown from 'antd/lib/statistic/Countdown';

export default (props) => {
    const location = useLocation();
    const navigate = useNavigate();
    const info = location.state.info;
    const startTime = ToStandard(info.startTime);
    const endTime = ToStandard(info.endTime);
    const [popupVisible, setPopupVisible] = useState(false);
    const [maskVisible, setMaskVisible] = useState(false);
    const [items, setItems] = useState(info.items);
    
    const imageArray = () => {
        let arr = [];
        for(let i = 0; i < items.length; ++i)
            if(items[i].itemImage)  arr.push(items[i].itemImage);
        return arr;
    }

    const images = imageArray().map((image, index) => (
        <Swiper.Item key={index}>
            <div className='littleImagesContainer'>
                <Image src={image} />
            </div>
        </Swiper.Item>
    ))

    const addItem = async (v) => {
        setMaskVisible(true);
        if(v.itemSeckill) {
            v["skStartTime"] = ToYYYYMMDD(v.skStartTime);
            v["skEndTime"] = ToYYYYMMDD(v.skEndTime);
        }
        v["groupId"] = info.groupId;
        v["itemPrice"] = PriceToInt(v.itemPrice);
        let response = await doJSONPost("/addItemToGroup", v);
        if(response.status == 0) {
            let result = await doGet('/searchGroupByGroupLeader/' + props.user.name);
            setItems(result[location.state.index].items);
            setMaskVisible(false);
            Toast.show("????????????");
        } else {
            setMaskVisible(false);
            Toast.show("????????????");
        }
    }

    const goBack = () => {
        navigate('/admin', { replace: true });
    }
  
    useEffect(()=>{
      window.history.pushState(null, null);
      window.addEventListener('popstate', goBack);
      console.log("??????");
      return () => {console.log("??????"); window.removeEventListener('popstate', goBack) };
    }, []);

    return (
        <div style={{width: '100%'}}>

            <Header title='????????????' onBack={ () => { window.history.back(); goBack(); }} right={
                <div><AddOutline fontSize={20} onClick={()=>{setPopupVisible(true)}} /></div>
            } />

            <List.Item title={
                <div style={{ paddingLeft: 10 }}>
                    <span style={{fontWeight: 'bold', color:'black', fontSize: 16}} onClick={() => alert(info.groupId)}>{info.groupTitle}</span>
                    {
                        (!info.status)?
                        <Tag color='default' style={{marginLeft: '5px'}}>?????????</Tag> :
                        (new Date(info.startTime) > new Date())?
                        <Tag color='primary' style={{marginLeft: '5px'}}>?????????</Tag> :
                        (new Date(info.endTime) > new Date())?
                        <Tag color='success' style={{marginLeft: '5px'}}>?????????</Tag> :
                        <Tag color='danger' style={{marginLeft: '5px'}}>?????????</Tag>
                    }
                    <div style={{ height: 20, marginTop: 2 }}>
                        <span style={{ float: 'left', marginRight: 5, fontSize: 12 }}>????????????????????????</span>
                        <Countdown style={{ float: 'left' }} valueStyle={{ fontSize:12 }} value={new Date(info.endTime)}  format="D ??? H ??? m ??? s ???" />
                    </div>
                    <div style={{marginRight: 10, height: '8px', borderBottomWidth: 1, borderBottomColor: '#ededed', borderBottomStyle: 'solid' }}></div>
                </div>
            }>
                <List.Item prefix={
                    <div className='littleImagesContainer'>
                        <Swiper loop autoplay style={{'--width': '60px', '--height': '60px'}} indicator={() => null}>
                            { images }
                        </Swiper>
                    </div>}
                    style={{paddingRight: 20}}
                >
                    <Ellipsis
                        direction='end'
                        content={'\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0' + (info.groupDescription? info.groupDescription : '??????????????????')}
                        expandText='??????'
                        collapseText='??????'
                        rows={3}
                    />
                </List.Item>
                
            </List.Item>

            <Tabs defaultActiveKey='1'>
                <Tabs.Tab title='????????????' key='1'>
                    <ProductList items={items} setItems={setItems} time={[info.startTime, info.endTime]} />
                </Tabs.Tab>
                <Tabs.Tab title='????????????' key='2'>
                {
                    info.orders != null && (info.orders.length > 0 ? info.orders.map((item, index) => {
                        return <OrderCard info={item} index={index} userid={props.user.userid} setGroupOrderList={v => {}} />;
                    }) : 
                    <Empty
                        style={{ padding: '64px 0'}}
                        image={ <FileWrongOutline style={{ color: 'var(--adm-color-primary)', fontSize: 96 }} />}
                        description={<h4 style={{color:"black"}}>????????????</h4>}
                    />)
                }
                </Tabs.Tab>
                <Tabs.Tab title='????????????' key='3' forceRender={true}>
                    <div style={{ width: '100%' }}><GroupStatistic groupId={info.groupId} /></div>
                </Tabs.Tab>
            </Tabs>

            <AddItemPopup visible={popupVisible} setVisible={setPopupVisible} onFinish={(v)=>{ addItem(v) }} append={true} startTime={startTime} endTime={endTime} />
        
            <LoadingMask visible={maskVisible} />
        </div>
    );
}