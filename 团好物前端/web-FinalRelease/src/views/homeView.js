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
    const isMobile = ['Android', 'iPhone', 'SymbianOS', 'Windows Phone', 'iPad', 'iPod'].filter(i =>
        navigator.userAgent.includes(i)
    ).length? true : false;
    const isWeiXin = (navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == "micromessenger");
    const [maskVisible, setMaskVisible] = useState(false);
    const IconFont = createFromIconfontCN({
        scriptUrl: [
            '//at.alicdn.com/t/c/font_3624788_ni9506bgnp.js',
        ],
    })
    const [reItems, setReItems] = useState(null)
    const [lastGroups, setLastGroups] = useState(null)
    const navigate = useNavigate()
    const location = useLocation()

    useEffect(()=>{
        navigator.clipboard && navigator.clipboard.readText().then((result) => {
            if(result.includes('/goods_view?groupId=') && result !== props.link) {
                props.setLink(result);
                let dst = result.substring(result.lastIndexOf('/'), result.indexOf('?'));
                let groupId = result.substring(result.indexOf('=') + 1);
                const getData = async() => {
                    let group = await doGet('/searchGroupByGroupId/' + groupId);
                    Dialog.confirm({
                        content: '您想要查看团购 ' + group.groupTitle +  ' 吗？',
                        onConfirm: () => {
                            navigate(dst, { state: { groupInfo: group, back: '/home', backState: null }, replace: true });
                        },
                    })
                }
                getData();
            }
        }).catch(e=>{
            console.log("读取剪贴板错误");
        });


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
    }, [])

    const openAPP = () => {
        let time = new Date().getTime();
        let timer = setInterval(() => {
            let t = new Date().getTime() - time;
            if(t > 2500 && t < 3500) {
                let a = document.createElement("a");
                a.href = window.location.protocol + "//" + window.location.hostname + ":8080/download/tuanhaowu.apk";
                a.click();
            }
            if(t > 3500)    clearInterval(timer);
        }, 1000);
    }

    return (
        <div style={{flex: 1}}>
            <Header title='首页' backArrow={false} right={
                isMobile && (isWeiXin?
                <a onClick={() => { window.history.pushState(null, null, window.location.protocol + "//" + window.location.host + "/app?home"); setMaskVisible(true) }}>打开APP</a> :
                <a href="thwapp://home" id="app" onClick={() => openAPP()}>打开APP</a>)}
            />
            <div style={{marginTop: '10px'}}>
                <Space block direction='vertical'>
                    <SearchBar placeholder='请输入内容' onSearch={val=>{
                        navigate('/search', { state: { txt: val }})
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

            <Mask visible={maskVisible} onMaskClick={() => { window.history.back(); setMaskVisible(false) } }>
                <div style={{ float: 'right', marginRight: '10px' }}>
                    <div>
                        <IconFont type="icon-aaa" style={{ fontSize: '80px', color: 'white', paddingLeft: '50px' }} />
                    </div>
                    <div>
                        <span style={{ fontSize: 18, color: 'white', fontWeight: 'bold' }}>点击右上角</span>
                        <MoreOutline fontSize={24} style={{ color:'white' }} />
                    </div>
                    <div>
                        <span style={{ fontSize: 18, color: 'white', fontWeight: 'bold' }}>在浏览器中打开</span>
                        <IconFont type='icon-anzhuoduanliulanqidakai' style={{ color: 'white', fontSize: '24px' }} />
                    </div>
                </div>
            </Mask>
        </div>
    );
}
export default HomeView;
