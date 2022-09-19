import {useNavigate} from "react-router-dom";
import {Avatar, Button, Card, Collapse, Image, List, Space, Swiper, Tag, TextArea, Toast} from "antd-mobile";
import {RightOutline} from "antd-mobile-icons";
import React from "react";
import {IntToPrice} from "../utils/transformPrice";
import {doGet} from "../utils/ajax";
import {CollapsePanel} from "antd-mobile/es/components/collapse/collapse";
import {SwiperItem} from "antd-mobile/es/components/swiper/swiper-item";
import {Badge} from "antd";

export default (props)=>{
    const navigate = useNavigate()
    const groupInfo = props.groupInfo
    console.log(groupInfo)

    const picItems = []
    if(groupInfo.items.length === 0) return
        groupInfo.items.forEach((item)=>{
            if(item.itemImage)
                picItems.push(item)
        })

    const SwItem = picItems.map((item, index)=>{
        return(
            <SwiperItem key={index} >
                <div style={{ borderRadius:10}}>
                    <Image src={item.itemImage}
                           width={120} height={120} style={{borderRadius:4}}/>
                    <div >
                        <div style={{fontSize:10, color:"darkgrey", wordBreak:"break-word", height:20, width:"inherit", margin:"auto" }}>
                            {item.itemName}
                        </div>
                        <div style={{fontSize:10, color:"black", wordBreak:"break-word", height:20, width:"inherit", margin:"auto" }}>
                            ¥{IntToPrice(item.itemPrice)}
                        </div>
                        <div style={{height:10}}/>
                    </div>
                </div>
            </SwiperItem>
        )
    })

    return(
        <div style={{borderRadius: '36px', position:"relative"}}>
            <Card
                title={
                    <div style={{ fontWeight: 'bold', fontSize:18, marginLeft:10 }}>
                        {groupInfo.groupTitle}
                    </div>
                }
                className='cardItem'
                extra={
                    <RightOutline />
                }
                onHeaderClick={()=>{
                    navigate('/group_info',{
                        replace: true,
                        state: { groupId:groupInfo.groupId, back:props.back, backState: props.backState } }
                    )

                }}
                style={{ borderRadius: '24px', alignItems: 'center'}}
            >
                <Swiper slideSize={33} stuckAtBoundary={false} >
                    {SwItem}
                </Swiper>

            </Card>
            <div style={{height: '10px', backgroundColor: '#ededed'}}>
            </div>
        </div>
    )
}
