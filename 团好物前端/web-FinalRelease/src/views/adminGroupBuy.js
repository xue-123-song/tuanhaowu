import React, { useEffect, useState } from 'react';
import Header from '../components/header';
import GroupBuyCard from '../components/groupBuyCard';
import { useNavigate } from 'react-router-dom';
import { getCreatedGroupBuy } from '../services/groupService';
import { Button, Space, List, Empty, DotLoading } from 'antd-mobile';
import { AddCircleOutline, FileWrongOutline } from 'antd-mobile-icons';
import 'antd/dist/antd.css';

export default (props) => {
    const navigate = useNavigate();
    const [groupBuyList, setGroupBuyList] = useState(props.groupBuyList);

    useEffect(()=>{
        const fetchData = async() => {
            let response = await getCreatedGroupBuy(props.user.name);
            setGroupBuyList(response);
            props.setGroupBuyList(response);
        }
        fetchData();
    }, [])

    return (
        <div className='app_container'>
            <Header back='' backArrow={false} title='我的团购' />
            <div style={{marginLeft: '3%', width: '94%'}}>
                <Button block type='button' onClick={() => { navigate('/create', {}); } } style={{borderColor: 'blue', marginTop: '10px'}}>
                <Space>
                    <AddCircleOutline />
                    <span>新建团购</span>
                </Space>
                </Button>
                <List header='我创建的团购'></List>
                <Space block direction='vertical'>
                {
                    groupBuyList != null ? (groupBuyList.length > 0 ? groupBuyList.map((item, index)=>{
                        return <GroupBuyCard info={item} key={index} groupBuyList={groupBuyList} setGroupBuyList={v => { setGroupBuyList(v); props.setGroupBuyList(v); }} />
                    }) : 
                    <Empty
                        style={{ padding: '64px 0'}}
                        image={ <FileWrongOutline style={{ color: 'var(--adm-color-primary)', fontSize: 96 }} />}
                        description={<h4 style={{color:"black"}}>暂无数据</h4>}
                    />) :                    
                    <div className="overlayContent">
                        <DotLoading style={{ '--size': '200px' }} />
                    </div>
                }
                </Space>
                <div style={{height: '50px', backgroundColor: '#ededed'}}></div>
            </div>
        </div>
    );
}