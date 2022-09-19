import React, { useState, useEffect } from 'react';
import { Navigate } from "react-router-dom";
import { checkAuth } from '../services/userService';

const RequireUnAuth = (props) => {
    const [isAuthed, setIsAuthed] = useState(false);
    const [hasAuthed, setHasAuthed] = useState(false);

    useEffect(() => {
        setIsAuthed(checkAuth());
        setHasAuthed(true);
    }, [])

    if (!hasAuthed)      return null;
    return (isAuthed? <Navigate to="/" replace/> : props.children);
}

export default RequireUnAuth;