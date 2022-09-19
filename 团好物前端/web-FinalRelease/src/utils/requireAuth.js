import React, { useState, useEffect } from 'react';
import { Navigate } from "react-router-dom";
import { checkAuth } from '../services/userService';

const RequireAuth = (props) => {
    const [isAuthed, setIsAuthed] = useState(false);
    const [hasAuthed, setHasAuthed] = useState(false);

    useEffect(() => {
        setIsAuthed(checkAuth());
        console.log("here");
        setHasAuthed(true);
    }, [])

    if (!hasAuthed)      return null;
    return (isAuthed? <div style={{ flex: 1 }}>{props.children}</div> : <Navigate to="/login" replace/>);
}

export default RequireAuth;
