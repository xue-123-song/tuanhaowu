import {useEffect, useState} from "react";
import {Badge, Empty, SearchBar, Tabs} from "antd-mobile";
import Header from "../components/header";
import React from "react";
import {doGet} from "../utils/ajax";
import {useLocation, useNavigate} from "react-router-dom";
import {FileWrongOutline} from "antd-mobile-icons";
import UserGroupCard from "../components/userGroupCard";

export default() =>{

    const location = useLocation()

    const [titleList, setTitleList] = useState([])
    const [leaderList, setLeaderList] = useState([])
    const [itemList, setItemList] = useState([])
    const [searchText, setSearchText] = useState(location.state.txt)

    // const back =()=>{
    //     navigate('/home', { replace: true });
    // }

    useEffect(()=>{
        async function search(){
            setLeaderList(await doGet('/tourist/FuzzyQueryByGroupLeader/' + searchText))
            setTitleList(await doGet('/tourist/FuzzyQueryByGroupTitle/' + searchText))
            setItemList(await doGet('/tourist/FuzzyQueryByItemName/' + searchText))
        }
        search()
        // window.history.pushState(null, null);
        // window.addEventListener('popstate', back);
        // console.log("监听");
        // return () => {console.log("移除"); window.removeEventListener('popstate', back) };
    },[])

    const Search = async (val)=>{
        console.log(val)
        if(val === "") return
        setSearchText(val)
        setLeaderList(await doGet('/tourist/FuzzyQueryByGroupLeader/' + val))
        setTitleList(await doGet('/tourist/FuzzyQueryByGroupTitle/' + val))
        setItemList(await doGet('/tourist/FuzzyQueryByItemName/' + val))
    }

    const navigate = useNavigate()



    const GroupMapping =(props)=>{
        console.log(props.groups)
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
                    description={<h4 style={{color:"black"}}>暂无数据</h4>}
                />
            )
        return (
            props.groups.map((group)=>{
                return( <UserGroupCard groupInfo={group} txt={searchText}/>)
            })
        )
    }

    return(
        <div style={{width:"100%"}}>
            <Header title='搜索结果' onBack={() => window.history.back()} />
            <SearchBar placeholder={searchText} onSearch={val => Search(val)}/>
            <div style={{width: '100%'}}>
                <Tabs defaultActiveKey='1'>
                    <Tabs.Tab title={"搜好物"} key='1'>
                        <GroupMapping groups={itemList}/>
                    </Tabs.Tab>
                    <Tabs.Tab title={"搜团购"} key='2'>
                        <GroupMapping groups={titleList}/>
                    </Tabs.Tab>
                    <Tabs.Tab title={"搜店长"} key='3'>
                        <GroupMapping groups={leaderList}/>
                    </Tabs.Tab>
                </Tabs>
            </div>
            <div style={{height: '50px', backgroundColor: '#ededed'}}></div>
        </div>
    )
}
