import React, { useEffect, useState } from 'react'
import Pages from './Router'
import { Route, Routes, MemoryRouter as Router } from 'react-router-dom'
import { decrypt } from './utils/encrypt';

export default () => {
  const [role, setRole] = useState();
  const [user, setUser] = useState();
  const [loading, setLoading] = useState(true);

  useEffect(()=>{
    let userInfo = decrypt(localStorage.getItem("user"));
    if(userInfo == false) {
      setUser(null);
      setRole("游客");
      console.log("游客");
      setLoading(false);
    } else {
      setUser(JSON.parse(userInfo));
      setRole(localStorage.getItem("role"));
      setLoading(false);
    }
    console.log(userInfo);
  }, []);
  if(loading) {
      return <></>;
  } else {
    return (
      <Router initialEntries={[(user === null)? '/login' : '/home']}>
          <Pages user={user} setUser={setUser} role={role} setRole={setRole} />
      </Router>
    );
  }
}


