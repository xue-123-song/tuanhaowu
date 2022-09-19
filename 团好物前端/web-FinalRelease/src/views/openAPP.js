import React, { useEffect } from "react";

const OpenAPPView = (props) => {
    const downloadURL = window.location.protocol + "//" + window.location.hostname + ":8080/download/tuanhaowu.apk";
    let search = window.location.search, groupId;
    search = search.substring(1);
    if(search.indexOf('group_info') != -1)    search = 'group_info?groupId=' + search.substring(search.indexOf('=') + 1);

    useEffect(() => {
        let element = document.getElementById("app");
        element.click();
        let time = new Date().getTime();
        let timer = setInterval(() => {
            let t = new Date().getTime() - time;
            if(t > 2500 && t < 3500)    document.getElementById("apk").click();
            if(t > 3500)    clearInterval(timer);
        }, 1000);
    }, []);

    console.log(search);

    return (
        <>
            <a href={downloadURL} id="apk"></a>
            <a href={"thwapp://" + search} id="app"></a>
        </>
    );
}

export default OpenAPPView;
