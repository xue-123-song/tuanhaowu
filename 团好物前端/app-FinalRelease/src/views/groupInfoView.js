import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import Header from "../components/header";
import React from "react";
import {doGet, doJSONPost} from "../utils/ajax";
import {
    AutoCenter,
    Avatar, Button,
    Collapse, Dialog,
    Divider,
    Ellipsis,
    Image,
    List,
    Popup,
    Space,
    Stepper, Tag,
    TextArea, Toast
} from "antd-mobile";
import {ListItem} from "antd-mobile/es/components/list/list-item";
import {DownOutline} from "antd-mobile-icons";
import {CollapsePanel} from "antd-mobile/es/components/collapse/collapse";
import {IntToPrice} from "../utils/transformPrice";
import Timer from "../utils/timer";
import Countdown from "antd/es/statistic/Countdown";
import useForceUpdate from "antd/es/_util/hooks/useForceUpdate";
import SkItemList from "../components/skItemList";
import {decrypt} from "../utils/encrypt";

const bg1_banner = '#8fca93'
const bg2_banner = '#f6f2eb'
const bg_list = '#665657'
const ft_list = '#e5f8fc'

export default (props) =>{

    const location = useLocation();

    const [groupId, setGroupId] = useState(location.state ? location.state.groupId : 1023)  //argument in link
    const [groupInfo, setGroupInfo] = useState([])
    const [onSale, setOnSale] = useState(null)

    const [visible, setVisible] = useState(false)
    const [imageVisible, setImageVisible] = useState(false) //后续
    const [popInfo, setPopInfo] = useState([])
    const [imageInfo, setImageInfo] = useState({
        index:1,
        urls:[]
    })

    const navigate = useNavigate()
    const back = () => {
        navigate(location.state.back, { replace: true, state:location.state.backState });
    }


    useEffect( ()=>{
            console.log(location.state)

            async function fetchGroupInfo(){
                let group = await doGet('/searchGroupByGroupId/' + groupId)
                if(group === null) {
                    await Dialog.alert({
                        content: '您查找的团购不存在或已被删除！'
                    })
                    navigate(location.state.back, { replace: true, state:location.state.backState })
                }
                setGroupInfo(group)
                setFocus(group.subscribeId > 0);
                if((new Date().getTime() > new Date(group.startTime).getTime()) &&
                    (new Date().getTime() < new Date(group.endTime).getTime()))
                    setOnSale(true)
                else setOnSale(false)
                console.log(group)
            }
            fetchGroupInfo()
            //if(group === null) return

        }
    ,[])

    const Logistics = (props) =>{
        if(props.logistics === 1)
            return (<div  style={{fontSize:13}}>快递</div>)
        else if(props.logistics === 2)
            return (<div  style={{fontSize:13}}>同城配送</div>)
        else if(props.logistics === 3)
            return (<div  style={{fontSize:13}}>顾客自提</div>)
        else if(props.logistics === 4)
            return (<div  style={{fontSize:13}}>无物流</div>)
    }

    const StartLabel = () => {
        console.log(onSale)
        if(new Date().getTime() < new Date(groupInfo.startTime).getTime())
            return <div style={{color:"grey", fontSize:14}}>团购未开始</div>
        if(new Date(groupInfo.endTime).getTime() < new Date())
            return <div style={{color:"grey", fontSize:14}}>团购已结束</div>
        return <div style={{color:"green", fontSize:14}}>正在热卖中</div>
    }

    //是否已关注， 后续应在groupInfo
    const [focus, setFocus] = useState(false);
    const user = props.user;

    const focusGroup = async () => {
        let response = await doJSONPost("/Subscribe/add", { userId: user.userid, groupId: groupInfo.groupId });
        if(response.status == 0) {
            Toast.show({ content: '已关注团购', position: 'bottom' })
            setFocus(true)
        } else {
            Toast.show({ content: '关注失败', position: 'bottom' })
        }
    }

    const FocusButton = () => {
      if(focus)
          return(
              <Button size={"mini"} style={{fontSize: 4, margin:"auto", "--border-radius":'10px',
                  "--background-color":'grey', "--border-color":'grey', "--text-color":bg2_banner}}
                      onClick={async () => {
                          const result = await Dialog.confirm({
                          content: '确定取消关注此团购吗？',
                      })
                          if (result) {
                              if(groupInfo.subscribeId > 0) {
                                  let response = await doGet("/Subscribe/cancel/" + groupInfo.subscribeId);
                                  if(response.status == 0) {
                                    Toast.show({ content: '已取消关注', position: 'bottom' })
                                    setFocus(false)
                                  } else {
                                    Toast.show({ content: '取消关注失败', position: 'bottom' })
                                  }
                              } else {
                                Toast.show({ content: '取消关注失败', position: 'bottom' })
                              }

                      } else {
                          Toast.show({ content: '点击了取消', position: 'bottom' })
                      }
                      }}>
                  已关注
              </Button>
          )
        else return (
          <Button size={"mini"} style={{fontSize: 4, margin:"auto", "--border-radius":'10px',
              "--background-color":bg1_banner, "--border-color":bg1_banner, "--text-color":bg2_banner}}
            onClick={()=> focusGroup() }>
              关注团购
          </Button>
      )
    }

    const Images = (props) =>{
        const imgs = []
        if(props.item.itemImage) imgs.push(props.item.itemImage)
        if(props.item.Image1) imgs.push(props.item.Image1)
        if(props.item.Image2) imgs.push(props.item.Image2)

        return(<>
            {
                imgs.map((img)=>{
                    return(
                        <Image src={popInfo.itemImage} width={100} height={100} fit='contain'
                               onContainerClick={()=>{
                                   if(popInfo.itemImage !== null)
                                       setImageInfo(popInfo.itemImage)
                                   setImageVisible(true)
                               }}/>
                    )
                })
            }
            </>
        )
    }


    return(
        <div style={{flex: 1}}>
            <Header onBack={back} title='团购详情' />
            <div style={{display:'flex', height:75, background: bg1_banner}}>
                <div style={{margin:'auto', color:bg2_banner, fontSize:'30px', fontFamily:'仿宋'}}>
                精品好物 欲购从速
                </div>
            </div>
            <div style={{display:'flex', height:40, background: bg1_banner}}>
                <div style={{display:'flex', marginBottom:'0px', marginLeft:'8%', borderTopLeftRadius:'20px',
                        borderTopRightRadius:'20px', background:bg2_banner, width:"60%"}}>
                    <div style={{margin:"auto", color:bg1_banner, fontSize:'18px', fontFamily:'仿宋', fontWeight:"bold"}}>
                        <Ellipsis content={groupInfo.groupTitle} />
                    </div>
                </div>
                <div style={{display:'flex', marginBottom:'0px', marginLeft:'4%', borderTopLeftRadius:'20px',
                    borderTopRightRadius:'20px', background:bg2_banner, width:"20%"}}>
                    <FocusButton/>
                </div>
            </div>
            <div>
                <List>
                    <ListItem arrow={true} onClick={()=>{
                        navigate('/leader_info',{ state : {leaderName:groupInfo.groupLeader} })
                    }}
                              title={'五星店长'}
                              extra={<div style={{fontSize:12}}>店长首页</div> }
                        prefix={
                        <div style={{display:"flex", padding:5}}>
                            <Avatar src={''} style={{margin:"auto"}}/>
                        </div>
                    }>
                        <div style={{color:'grey', margin:"auto", fontSize:16}}>{groupInfo.groupLeader}</div>
                    </ListItem>
                    <ListItem title={'团购简介'}>
                        <div>
                            <Ellipsis style={{width:'100%', wordBreak:'break-word', fontSize:14}} content={groupInfo.groupDescription}
                                      expandText='展开'
                                      collapseText='收起'/>
                        </div>
                    </ListItem>
                    <ListItem prefix={<div style={{fontSize:13, color:"grey"}}> 物流方式 </div>}>
                        <Logistics logistics={groupInfo.logistics}/>
                    </ListItem>
                    <ListItem title={'起止时间'} extra={
                        <StartLabel/>
                    }>
                        <div style={{fontSize:14}}>
                            {groupInfo.startTime} ~ {groupInfo.endTime}
                        </div>
                    </ListItem>
                </List>
                    <div style={{background:bg_list}}>
                        <Collapse  defaultActiveKey={['1']}>
                            <CollapsePanel style={{background:bg_list}}  key={1} title={
                                <Divider contentPosition={"left"} style={{
                                    color:ft_list,
                                    borderColor:ft_list,
                                    fontSize:16
                                }}>秒杀商品</Divider>}
                            >
                                <div style={{borderRadius:10, background:bg_list, border:'8px solid #665657'}}>
                                <List>
                                    {
                                        groupInfo.items && groupInfo.items.map((item, index) => {
                                            if(!item.itemSeckill) return
                                        return(
                                            <SkItemList item={item} setPopInfo={setPopInfo} setVisible={setVisible}/>
                                            )
                                     })
                                     }
                                </List>
                                </div>
                            </CollapsePanel>
                        </Collapse>
                        <Collapse  defaultActiveKey={['1']}>
                            <CollapsePanel style={{background:bg_list}} key={1} title={
                                <Divider contentPosition={"left"} style={{
                                    color:ft_list,
                                    borderColor:ft_list,
                                    fontSize:16
                                }}>团购商品</Divider>}
                            >
                                <div style={{borderRadius:10, background:bg_list, border:'8px solid #665657'}}>
                                    <List >
                                        {
                                            groupInfo.items && groupInfo.items.map((item, index) => {
                                                if(item.itemSeckill) return
                                                return (
                                                    <>
                                                            <List.Item
                                                                // prefix={<Image style={{borderRadius: 6}} src={item.itemImage} />}
                                                                prefix={<Avatar src={item.itemImage} fit='contain' /> }
                                                                extra={
                                                                    <Space style={{fontSize:12}}>
                                                                        剩余{item.itemStock}件
                                                                    </Space>
                                                                }
                                                                onClick={() => {
                                                                    setPopInfo(item)
                                                                    setVisible(true)
                                                                }}
                                                            >
                                                                <div style={{fontSize:16, color:"black"}}>
                                                                    {item.itemName}
                                                                </div>
                                                            </List.Item>
                                                            <List.Item extra={
                                                                <div style={{fontSize:18, color:"black", fontWeight:"bold"}}>¥{IntToPrice(item.itemPrice)}</div>
                                                            }>
                                                                <div style={{fontSize:16, color:"grey"}}>单价</div>
                                                            </List.Item>
                                                    </>
                                                )
                                            })
                                        }
                                    </List>
                                </div>
                            </CollapsePanel>
                        </Collapse>
                        <div style={{background:bg_list, height:60}}>
                            <div style={{marginTop:18, marginLeft:'10%', marginRight:'10%'}}>
                                <Button block style={{"--background-color":ft_list, "--border-color":ft_list, "--text-color":bg_list, fontWeight:"bold"}}
                                onClick={()=>{
                                    if(onSale)
                                        navigate('/goods_view', {state:{groupInfo: groupInfo, back: '/group_info', backState:{groupId: groupId}} })
                                    else Toast.show('团购已结束')
                                }}>
                                    前往购买
                                </Button>
                            </div>
                        </div>
                    </div>

                <Popup visible={visible}
                       onMaskClick={() => {
                           setVisible(false)
                       }}
                       bodyStyle={{
                           borderTopLeftRadius: '16px',
                           borderTopRightRadius: '16px',
                           minHeight: '60vh', }}
                >
                    <List>
                        <List.Item
                            prefix={
                                <div style={{color:"grey"}}>商品</div>
                            }
                            extra={
                                <Space>
                                    剩余
                                    {popInfo.itemStock}
                                    件
                                </Space>
                        }>
                            <Space>
                                {popInfo.itemName}
                            </Space>
                        </List.Item>
                        <List.Item prefix={<div style={{color:"grey"}}>图片</div>}>
                            <Space wrap={true}>
                                <Images item={popInfo}/>
                            </Space>
                        </List.Item>
                        <List.Item extra={<div style={{fontSize: 20, fontWeight: 700, color: 'red'}}>￥{IntToPrice(popInfo.itemPrice)}</div>}>
                            <div style={{color:"grey"}}>单价</div>
                        </List.Item>
                        <List.Item prefix={<div style={{color:"grey"}}>详情</div>}>
                            <TextArea readOnly autoSize style={{fontSize:8}} value={popInfo.itemDescription}/>
                        </List.Item>
                    </List>
                </Popup>
            </div>
        </div>
    )
}
