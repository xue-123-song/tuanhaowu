import { useEffect, useState } from 'react'
import { Route, Routes, MemoryRouter as Router, useNavigate } from 'react-router-dom'
import Bottom from './components/bottom'
import AdminGroupBuy from './views/adminGroupBuy'
import CreateGroupBuy from './views/createGroupBuy'
import './App.less'
import 'antd-mobile/es/global/global.css'
import ViewProducts from './views/viewProducts'
import Header from './components/header'
import OrderView from './views/orderView'
import PersonalCenter from './views/personCenter'
import SwitchRole from './views/switchRole'
import PersonInfo from './views/personInfo'
import ScanCode from './views/scanCode'
import HomeView from "./views/homeView";
import UserGroupBuy from "./views/userGroupBuy";
import FollowGroupBuy from "./views/followGroupBuy";
import UserOrderManager from "./views/userOrderManager";
import LoginView from './views/loginView';
import RegisterView from './views/registerView'
import PayOrderView from "./views/payOrderView";
import SearchResultView from "./views/searchResultView";
import ViewGoods from "./views/viewGoods";
import ViewLogistics from './views/viewLogistics';
import LeaderView from "./views/leaderView";
import LocationView from "./views/locationView";
import GroupInfoView from "./views/groupInfoView";
import MessageCenter from './views/messageCenter';

export default (props) => {
    const [groupBuyList, setGroupBuyList] = useState(null);
    const [groupOrderList, setGroupOrderList] = useState(null);
    const [link, setLink] = useState('');
    const { role, setRole, user, setUser } = props;

    const navigate = useNavigate();

    useEffect(() => {
        const checkArguments = () => {
            if(window.plus) {
                let args = window.plus.runtime.arguments;
                if(typeof(args) === 'string' && args.indexOf("thwapp") != -1) {
                    try {
                        let path = args.substring(args.lastIndexOf("//") + 1);
                        if(path.indexOf('/group_info') != -1) {
                            let groupId = Number(path.substring(path.indexOf('=') + 1));
                            navigate('/group_info',{
                                replace: true,
                                state: { groupId:groupId, back:'/home', backState: null  } }
                            )
                        }
                        // let index = path.indexOf("?"), params = null;
                        // if(index != -1) {
                        //     params = path.substring(index + 1);
                        //     console.log(params);
                        //     path = path.substring(0, index);
                        // }
                        // console.log(path);
                        // navigate(path, {});
                    } catch(e) {
                        console.log(e);
                    }
                }
            } else {
                alert("plus not ready");
            }
        }
        document.addEventListener('newintent', checkArguments);
        return ()=>document.removeEventListener('newintent', checkArguments);
    }, []);

    useEffect(() => {
        const checkArguments = () => {
            let args = window.plus.runtime.arguments;
            if(typeof(args) === 'string' && args.indexOf("thwapp") != -1) {
                try {
                    let path = args.substring(args.lastIndexOf("//") + 1);
                    if(path.indexOf('/group_info') != -1) {
                        let groupId = Number(path.substring(path.indexOf('=') + 1));
                        navigate('/group_info',{
                            replace: true,
                            state: { groupId:groupId, back:'/home', backState: null  } }
                        )
                    }
                    // let index = path.indexOf("?"), params = null;
                    // if(index != -1) {
                    //     params = path.substring(index + 1);
                    //     console.log(params);
                    //     path = path.substring(0, index);
                    // }
                    // console.log(path);
                    // navigate(path, {});
                } catch(e) {
                    console.log(e);
                }
            }
        }
        document.addEventListener('plusready', checkArguments);
        return ()=>document.removeEventListener('plusready', checkArguments);
    }, []);

    useEffect(() => {
        const checkPush = () => {
            window.plus.push.addEventListener("click", function(msg) {
				alert(JSON.stringify(msg));
                let payload = msg.payload;
                if(typeof(payload) === 'string' && payload.indexOf('/') != -1) {
                    if(payload.indexOf('/group_info') != -1) {
                        let groupId = Number(payload.substring(payload.indexOf('=') + 1));
                        navigate('/group_info',{
                            replace: true,
                            state: { groupId:groupId, back:'/home', backState: null  } }
                        )
                    }
                }
			});
        }
        if(window.plus) checkPush();
        else document.addEventListener('plusready', checkPush);
    }, []);

    return (
        <div className="app_app">
        <div className="app_body">
          <Routes>
            <Route path='/home' element={<HomeView link={link} setLink={setLink} />} /> {/*首页*/}
            <Route path='search' element={<SearchResultView />} /> {/*搜索页*/}
            <Route path='/admin' element={<AdminGroupBuy user={user} groupBuyList={groupBuyList} setGroupBuyList={setGroupBuyList} />} />
            <Route path='/me' element={<PersonalCenter role={role} user={user} setUser={setUser} />} />
            <Route path='/create' element={<CreateGroupBuy user={user} />} />
            <Route path='/view' element={<ViewProducts user={user} />} />
            <Route path='/logistics_view' element={<ViewLogistics />} />
            <Route path='/goods_view' element={<ViewGoods />} />  {/*商品详情页（下订单）*/}
            <Route path='/order' element={<OrderView user={user} groupOrderList={groupOrderList} setGroupOrderList={setGroupOrderList} />} />
            <Route path='/switch' element={<SwitchRole role={role} setRole={setRole} />} />
            <Route path='/info' element={<PersonInfo user={user} setRole={setRole} setUser={setUser} />} />
            <Route path='/scan' element={<ScanCode />} />
            <Route path='/user' element={<UserGroupBuy />} /> {/*团购历史? 查找具体团购*/}
            <Route path='/follow' element={<FollowGroupBuy />} /> {/*单团购选购*/}
            <Route path='/user_order' element={<UserOrderManager />} /> {/*订单界面*/}
            <Route path='/pay' element={<PayOrderView/>} />
            <Route path='/login' element={<LoginView setRole={setRole} setUser={setUser} />} />
            <Route path='/register' element={<RegisterView />} />
            <Route path='/location' element={<LocationView />} />
            <Route path='/group_info' element={<GroupInfoView user={user} />} />
            <Route path='/leader_info' element={<LeaderView />} />
            <Route path='/message' element={<MessageCenter />} />
          </Routes>
        </div>
        <Bottom role={role} />
      </div>
    );
}