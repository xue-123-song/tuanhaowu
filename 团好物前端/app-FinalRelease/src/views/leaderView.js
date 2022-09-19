import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import Header from "../components/header";
import React from "react";
import {doGet} from "../utils/ajax";
import {AutoCenter, Button, Dialog, Divider, Empty, Image, Toast} from "antd-mobile";
import {FileWrongOutline} from "antd-mobile-icons";
import UserGroupCard from "../components/userGroupCard";

const bg1_banner = '#8fca93'
const bg2_banner = '#f6f2eb'
const bg_list = '#665657'
const ft_list = '#e5f8fc'

export default () => {

    const location = useLocation();
    const navigate = useNavigate();

    const result = new URLSearchParams(window.location.search)
    const [leaderName, setLeaderName] = useState(location.state? location.state.leaderName : result.get('leaderName'))

    const [leaderInfo, setLeaderInfo] = useState(null)
    const [focus, setFocus] = useState(false)

    const back = () =>{
        if(!location.state) navigate('/');
        else navigate(-1, {state:location.state.backState});
    }

    useEffect(()=>{
        async function fetchLeader(){
            console.log(leaderName)
            let groups = await doGet('/searchGroupByGroupLeader/' + leaderName);
            console.log(groups)
            if(groups) {
                setLeaderInfo(groups)
                console.log(leaderInfo)
            }
        }
        fetchLeader()

    },[])

    const FocusButton = () => {
        if(focus)
            return(
                <Button size={"mini"} style={{fontSize: 4, margin:"auto", "--border-radius":'10px',
                    "--background-color":'grey', "--border-color":'grey', "--text-color":bg2_banner}}
                        onClick={async () => {
                            const result = await Dialog.confirm({
                                content: '确定取消关注吗？',
                            })
                            if (result) {
                                Toast.show({ content: '已取消关注', position: 'bottom' })
                                setFocus(false)
                            } else {
                                Toast.show({ content: '点击了取消', position: 'bottom' })
                            }
                        }}>
                    正在关注
                </Button>
            )
        else return (
            <Button size={"mini"} style={{fontSize: 4, margin:"auto", "--border-radius":'10px',
                "--background-color":bg1_banner, "--border-color":bg1_banner, "--text-color":bg2_banner}}
                    onClick={()=>{
                        Toast.show({ content: '已关注团长', position: 'bottom' })
                        setFocus(true)
                    }}>
                关注团长
            </Button>
        )
    }

    const GroupMapping =(props)=>{
        console.log(props.groups)
        if(!props.groups) return
        if(props.groups.length === 0)
            return(
                <Empty
                    style={{ padding: '64px 0'}}
                    image={
                        <FileWrongOutline
                            style={{
                                color: 'var(--adm-color-primary)',
                                fontSize: 96,
                            }}
                        />}
                    description={<h4 style={{color:"black"}}>ta还没有创建团购</h4>}
                />
            )
        return (
            props.groups.map((group)=>{
                return( <UserGroupCard groupInfo={group} back={'/leader_info'} backState={{leaderName:leaderName}}/>)
            })
        )
    }

    return(
        <div style={{flex: 1}}>
            <Header onBack={back} title='团长详情' />
            <div>
            <div style={{height:90, background: bg1_banner, position:"relative"}}/>
            <div style={{position:"absolute",top:100, left:20, height:60, width:60, zIndex:2 }}>
                <Image fit={"fill"} height={60} width={60} style={{borderRadius:8}}
                    src={''}/>
            </div>
            <div style={{backgroundColor:bg2_banner, position:"absolute", top:125, left:0,
                zIndex:1, height:100, width:'100%', borderTopRightRadius:16, borderTopLeftRadius:16}}>
                <div style={{marginLeft:20, marginTop:40, width:60, color:"black", wordBreak:"break-word", fontSize:14}}>
                    <AutoCenter>我是团长</AutoCenter>
                </div>
                <div style={{position:"absolute", top:20, left:"75%", width:80}}><FocusButton/></div>

            </div>
            </div>
            <div style={{marginTop:105}}>
                <Divider style={{fontStyle:16}}>ta创建的团购</Divider>
                <GroupMapping groups={leaderInfo}/>
            </div>
        </div>
    )

}
