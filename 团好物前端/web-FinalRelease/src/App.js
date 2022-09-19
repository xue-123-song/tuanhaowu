import React, { useEffect, useState } from 'react';
import Router from './Router';
import { decrypt } from './utils/encrypt';
import 'antd-mobile/es/global/global.css';
import './App.less';

export default () => {
    const [role, setRole] = useState();
    const [user, setUser] = useState();
    const [loading, setLoading] = useState(true);

    useEffect(()=>{
        let userInfo = decrypt(localStorage.getItem("user"));
        if(userInfo == null) {
          setUser(null);
          setRole('游客');
          setLoading(false);
        } else {
          setUser(JSON.parse(userInfo));
          setRole(localStorage.getItem("role"));
          setLoading(false);
        }
        console.log(userInfo);
    }, []);
    
    return loading? <></> : <Router role={role} setRole={setRole} user={user} setUser={setUser}  />
}


