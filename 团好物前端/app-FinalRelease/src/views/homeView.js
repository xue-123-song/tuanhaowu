import {Divider, SearchBar, Space, Swiper, Toast, Dialog, Mask, Tabs} from "antd-mobile";
import Header from "../components/header";
import ShopCard from "../components/shopCard";
import {useEffect, useState} from "react";
import {doGet, doJSONPost} from "../utils/ajax";
import {Col, Row} from "antd";
import React from "react";
import {useLocation, useNavigate} from "react-router-dom";
import { createFromIconfontCN } from '@ant-design/icons';
import { MoreOutline } from "antd-mobile-icons";
import {decrypt} from "../utils/encrypt";


const colors = ['#ace0ff', '#bcffbd', '#e4fabd', '#ffcfac'];

const items = colors.map((color, index) => (
    <Swiper.Item key={index}>
        <div
            className="content"
            style={{ background: color }}
            onClick={() => {
                Toast.show(`你点击了卡片 ${index + 1}`)
            }}
        >
            {index + 1}
        </div>
    </Swiper.Item>
))

const HomeView = (props)=>{
    const navigate = useNavigate();


    const [reItems, setReItems] = useState(null)
    const [lastGroups, setLastGroups] = useState(null)

    useEffect(()=>{
        async function getRecommend(){
            console.log(JSON.parse(decrypt(localStorage.getItem("user"))).userid)
            let userInfo = {userId: JSON.parse(decrypt(localStorage.getItem("user"))).userid }
            console.log(userInfo)
            // setReItems(await doJSONPost('/tourist/recommend', userInfo))
            // setLastGroups(await doGet('/tourist/getLatestGroup'))
            let items = await doJSONPost('/tourist/recommend', userInfo)
            let groups = await doGet('/tourist/getLatestGroup')
            if(!items.error)
                console.log(items)
            console.log(groups)
            setReItems(items)
            setLastGroups(groups)
        }
        getRecommend()
    },[])

    useEffect(()=>{
        const readText = () => {
            try {
                let Context = window.plus.android.importClass("android.content.Context");
                let main = window.plus.android.runtimeMainActivity();
                let clip = main.getSystemService(Context.CLIPBOARD_SERVICE);
                let result = window.plus.android.invoke(clip, "getText");
                if(result.includes('/group_info?groupId=') && result !== props.link) {
                    props.setLink(result);
                    let dst = result.substring(result.lastIndexOf('/'), result.indexOf('?'));
                    if(dst === '/group_info') {
                        let groupId = result.substring(result.indexOf('=') + 1);
                        const getData = async() => {
                            let group = await doGet('/searchGroupByGroupId/' + groupId);
                            Dialog.confirm({
                                content: '您想要查看团购 ' + group.groupTitle +  ' 吗？',
                                onConfirm: () => {
                                    navigate('/group_info', { state: { groupId: group.groupId, back: '/home', backState: null }, replace: true });
                                },
                            })
                        }
                        getData();
                    }
                }
            } catch (e) {
                console.log(e);
            }
        }
        if(window.plus) {
            readText();
        } else {
            document.addEventListener('plusready', readText);
            return () => document.removeEventListener('plusready', readText);
        }
    }, [])

    return (
        <div style={{flex: 1}}>
            <Header title='首页' backArrow={false} />
            <div style={{marginTop: '10px'}}>
                <Space block direction='vertical'>
                    <SearchBar placeholder='搜好物 / 团购 / 店长' onSearch={val=>{
                        navigate('/search', { replace: true, state: { txt: val }})
                    }}/>
                </Space>
                <Tabs defaultActiveKey={1}>
                    <Tabs.Tab title={'好物推荐'} key={1}>
                        <div style={{marginLeft: '5%', width: '90%'}}>
                            <Row gutter={8}>
                                {
                                    reItems && reItems.map((item, index)=>{
                                        return(
                                            <Col span={12}>
                                                <ShopCard item={item} key={index}/>
                                            </Col>
                                        )
                                    })
                                }
                            </Row>
                        </div>
                        <Divider>没有更多了</Divider>
                    </Tabs.Tab>
                    <Tabs.Tab title={'最新团购'} key={2}>
                        <div style={{marginLeft: '5%', width: '90%'}}>
                            <Row gutter={8}>
                                {
                                    lastGroups && lastGroups.map((group, index)=>{
                                        if(group.items.length === 0) return
                                        return(
                                            <Col span={12}>
                                                <ShopCard groupInfo={group} key={index}/>
                                            </Col>
                                        )
                                    })
                                }
                            </Row>
                        </div>
                        <Divider>没有更多了</Divider>
                    </Tabs.Tab>
                    <Tabs.Tab title={'热门团购'} key={3}>
                        <div style={{marginLeft: '5%', width: '90%'}}>
                            <Row gutter={8}>
                                {
                                    lastGroups && lastGroups.map((group, index)=>{
                                        if(group.items.length === 0) return
                                        if(index === 0 || index === 3) return
                                        return(
                                            <Col span={12}>
                                                <ShopCard groupInfo={group} key={index}/>
                                            </Col>
                                        )
                                    })
                                }
                            </Row>
                        </div>
                        <Divider>没有更多了</Divider>
                    </Tabs.Tab>
                </Tabs>
            </div>
            <div style={{height: '60px', backgroundColor: '#ededed'}}></div>
        </div>
    );
}
export default HomeView;
