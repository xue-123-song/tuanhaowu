import React, { useState } from 'react';
import { Route, Routes, BrowserRouter as Router } from 'react-router-dom';
import ViewProducts from './views/viewProducts';
import RequireAuth from './utils/requireAuth';
import RequireUnAuth from './utils/requireUnAuth';
import Bottom from './components/bottom'
import OrderView from './views/orderView';
import PersonalCenter from './views/personCenter';
import SwitchRole from './views/switchRole';
import PersonInfo from './views/personInfo';
import ScanCode from './views/scanCode';
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
import AdminGroupBuy from './views/adminGroupBuy';
import CreateGroupBuy from './views/createGroupBuy';
import OpenAPPView from './views/openAPP';
import LocationView from "./views/locationView";
import GroupInfoView from "./views/groupInfoView";
import LeaderView from "./views/leaderView";

export default (props) => {
    const { user, setUser, role, setRole } = props;
    const [link, setLink] = useState('');
    const [groupBuyList, setGroupBuyList] = useState(null);
    const [groupOrderList, setGroupOrderList] = useState(null);

    return (
        <Router>
            <div className="app_app">
                <div className="app_body">
                    <Routes>
                        <Route path='/' element={<HomeView link={link} setLink={setLink} />} /> {/*首页*/}
                        <Route path='/search' element={<SearchResultView />} /> {/*搜索页*/}
                        <Route path='/admin' element={<RequireAuth><AdminGroupBuy user={user} groupBuyList={groupBuyList} setGroupBuyList={setGroupBuyList} /></RequireAuth>} />
                        <Route path='/me' element={<PersonalCenter role={role} user={user} setUser={setUser} />} />
                        <Route path='/create' element={<RequireAuth><CreateGroupBuy user={user} /></RequireAuth>} />
                        <Route path='/view' element={<RequireAuth><ViewProducts user={user} /></RequireAuth>} />
                        <Route path='/logistics_view' element={<RequireAuth><ViewLogistics /></RequireAuth>} />
                        <Route path='/goods_view' element={<ViewGoods />} />  {/*商品详情页（下订单）*/}
                        <Route path='/order' element={<RequireAuth><OrderView user={user} groupOrderList={groupOrderList} setGroupOrderList={setGroupOrderList} /></RequireAuth>} />
                        <Route path='/switch' element={<RequireAuth><SwitchRole role={role} setRole={setRole} /></RequireAuth>} />
                        <Route path='/info' element={<RequireAuth><PersonInfo user={user} setRole={setRole} setUser={setUser} /></RequireAuth>} />
                        <Route path='/scan' element={<ScanCode />} />
                        <Route path='/user' element={<RequireAuth><UserGroupBuy /></RequireAuth>} /> {/*团购历史? 查找具体团购*/}
                        <Route path='/follow' element={<RequireAuth><FollowGroupBuy /></RequireAuth>} /> {/*单团购选购*/}
                        <Route path='/user_order' element={<RequireAuth><UserOrderManager /></RequireAuth>} /> {/*订单界面*/}
                        <Route path='/pay' element={<RequireAuth><PayOrderView/></RequireAuth>} />
                        <Route path='/login' element={<RequireUnAuth><LoginView setRole={setRole} setUser={setUser} /></RequireUnAuth>} />
                        <Route path='/register' element={<RequireUnAuth><RegisterView /></RequireUnAuth>} />
                        <Route path='/app' element={<OpenAPPView />} />
                        <Route path='/location' element={<LocationView />} />
                        <Route path='/group_info' element={<GroupInfoView />} />
                        <Route path='/leader_info' element={<LeaderView />} />
                    </Routes>
                </div>
                <Bottom role={role} />
            </div>
        </Router>
    );
}
