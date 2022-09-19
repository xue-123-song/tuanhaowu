import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LeftOutline } from 'antd-mobile-icons';
import { doGet } from '../utils/ajax';
import Header from "../components/header";

let barcode = null;
export default () => {
    const navigate = useNavigate();
    const goBack = () => {
        console.log("回退");
        navigate("/me");
    }
  
    useEffect(()=>{
      window.history.pushState(null, null);
      window.addEventListener('popstate', goBack);
      console.log("监听");
      return () => {console.log("移除"); window.removeEventListener('popstate', goBack); };
    }, []);

  useEffect(()=> {
    if (window.plus) {
        plusReady();
    } else {
        document.addEventListener('plusready', plusReady, false);
    }
    return ()=>{
        barcode&&barcode.close();
        barcode = null;
    }
  }, [])

  const plusReady = () => {
      console.log("ready");
     creatBarCode()
  }

  const creatBarCode = () => {
    if (!barcode) {
      barcode = window.plus.barcode.create('barcode', [window.plus.barcode.QR], {
        background: '#fff',
        frameColor: '#07c160',
        scanbarColor: '#07c160',
        top: '70px',
        left: '0px',
        width: '100%',
        height: '600px',
        position: 'static'
      });
      barcode.onmarked = onmarked   //扫码成功
      barcode.onerror = onerror //扫码失败
      window.plus.webview.currentWebview().append(barcode);
    }
    console.log(barcode);
    barcode.start();
  }

  // 识别成功
  function onmarked(type, result) {
    if(typeof(result) === 'string' && result.indexOf("/group_info") != -1) {
      let groupId = parseInt(result.substring(result.indexOf("groupId=") + 8));
      // const getData = async (groupId) => {
      //     let group = await doGet('/searchGroupByGroupId/' + groupId)
      //     console.log(group)
      //     if(!group.groupId) {
      //         window.plus.nativeUI.toast('未找到团购');
      //         barcode.start();
      //         return;
      //     }
          navigate('/group_info', {
              state: { groupId: groupId, back: '/scan', backState:null } }
          )
      //}
      //getData(groupId);
    } else {
      alert('扫描结果:' + result);
      barcode.start();
    }
  }

  // 识别失败
  function onerror(err){
    window.plus.nativeUI.toast('扫码失败');
    console.log('扫码失败',JSON.stringify(err)) 
    barcode&&barcode.close();
    barcode = null;
    creatBarCode()
  }

  // 从相册中选择
  function scanFromPic() {
    window.plus.gallery.pick(function (path) {
      window.plus.barcode.scan(path, onmarked, onerror);
    }, function (err) {
      console.log('选择相片失败: ' + JSON.stringify(err.message));
    });
  }

  return (
    <div style={{ flex: 1 }}>
          <Header title="扫一扫" onBack={()=>{ window.history.back(); goBack(); }} backArrow={<LeftOutline />} right={<div onClick={() => scanFromPic()}>从相册选择</div>} />
          <div >
              <div id="barcode"></div>
          </div>
    </div>
  )
}