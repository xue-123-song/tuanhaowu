import Header from "../components/header";
import React, {useEffect, useState} from "react";
import {Map, Marker, NavigationControl, InfoWindow} from 'react-bmapgl';
import {useNavigate} from "react-router-dom";
import {CityListControl} from "react-bmapgl/dist";
import UserGroupCard from "../components/userGroupCard";
import {MapComponent} from "../components/statistic";
import {RightOutline, ShopbagOutline} from "antd-mobile-icons";
import {Card, Image, Popup, Swiper} from "antd-mobile";

export default ()=>{

    const navigate = useNavigate()

    const goBack = () => {
        console.log("回退");
        navigate(-1);
    }

    const [lng, setLng] = useState(116.331398)
    const [lat, setLat] = useState(39.897445)

    const [groupInfo, setGroupInfo] = useState(null)
    const [visible, setVisible] = useState(false)

    const [groups,setGroups] = useState(null)

    useEffect(()=> {


            var geolocation = new window.BMapGL.Geolocation();
            // 开启SDK辅助定位
            geolocation.enableSDKLocation();

            geolocation.getCurrentPosition(function(r){
                console.log(this.getStatus())
                console.log(r.address)
                console.log(r.point)
                setLng(r.point.lng)
                setLat(r.point.lat)

                const myGeo = new window.BMapGL.Geocoder();
                myGeo.getLocation(new window.BMapGL.Point(r.point.lng, r.point.lat), function(res){
                    if (res){
                    } else {
                        console.log('--根据坐标得到地址描述失败--')
                    }
                })
            });

            setGroups([
                {
                    endTime: "2022-09-01 21:25:04",
                    groupDescription: "全场图书只需9块9，真的骗你是小狗",
                    groupId: 1023,
                    groupLeader: "goL",
                    groupTitle: "图书特卖，只要998",
                    items: [{
                        itemImage:'https://img.zcool.cn/community/01c5f35aa8fef8a80121246d06ce19.jpg@1280w_1l_2o_100sh.jpg',
                        itemPrice:7860
                    }],
                    logistics: 4,
                    orders: [],
                    picture: null,
                    startTime: "2022-07-04 18:12:57",
                    status: 1,
                    lat: 31.027015861663,
                    lng: 121.43910226882
                }
            ])




        }
,[])

    return(
        <div style={{flex: 1}}>
            <Header title='附近团购' onBack={goBack} />
            <Map  center={{lng: 121.43910226882, lat: 31.02600785712}} zoom="18" style={{height:'90%', width:'100%'}}>
                {/*<MapLoc />*/}
                <Marker position={{lng: 121.43910226882, lat: 31.02600785712}} icon={'loc_blue'}/>
                <CityListControl />
                {
                    groups && groups.map((group, index)=>{
                        return(
                            <Marker position={{lng:group.lng, lat: group.lat}} onClick={()=>{
                                setGroupInfo(group)
                                setVisible(true)
                            }}
                            />
                        )
                    })
                }
            </Map>
            <Popup visible={visible} onMaskClick={()=>{setVisible(false)}} style={{height:'40%'}}>
                <UserGroupCard groupInfo={groupInfo} back={'/location'} backState={null}/>
            </Popup>
        </div>
    )
}
