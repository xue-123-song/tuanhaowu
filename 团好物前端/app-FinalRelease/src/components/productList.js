import React, { useState } from 'react';
import { Button, Image, Form, Input, Tag, List, SwipeAction, Badge, ImageUploader, TextArea, Checkbox, DatePicker, Ellipsis, Popover, Popup, Stepper, Toast, Swiper } from 'antd-mobile';
import { PriceToInt, IntToPrice } from '../utils/transformPrice';
import { imageUpload } from '../utils/imageUpload';
import { ToYYYYMMDD, ToDisplay } from '../utils/transformTime';
import { doGet, doJSONPost } from '../utils/ajax';
import LoadingMask from './loadingMask';
import Countdown from 'antd/lib/statistic/Countdown';

const ProductList = (props) => {
    const [maskVisible, setMaskVisible] = useState(false);

    return (
        <div style={{height: '10px', backgroundColor: 'lightblue'}}>
            <List>
            {
                props.items.map((_, index)=>{
                    return <ProductListItem index={index} items={props.items} setItems={props.setItems} time={props.time} setMaskVisible={setMaskVisible} />
                })
            }
            </List>
            <LoadingMask visible={maskVisible} />  
        </div>
    );
}

const ProductListItem = (props) => {
    const index = props.index;
    const items = props.items;
    const [itemInfo, setItemInfo] = useState(items[props.index]);
    const [modifyVisible, setModifyVisible] = useState(false);

    const modifyItem = async (item) => {
        props.setMaskVisible(true);
        item["itemId"] = items[index].itemId;
        item["itemPrice"] = PriceToInt(item["itemPrice"]);
        item["itemSeckill"] = (item.itemSeckill? true : false);
        console.log(item);
        let response = await doJSONPost("/modifyItemInfo", item);
        if(response.status == 0) {
            console.log("fresh");
            if(item.itemImage) {
                if(item["itemImage"].length > 2)    item["image2"] = item["itemImage"][2].url;
                if(item["itemImage"].length > 1)    item["image1"] = item["itemImage"][1].url;
                item["itemImage"] = item["itemImage"][0].url;
            }
            console.log(item)
            setItemInfo(item);
        }
        props.setMaskVisible(false);
        Toast.show(response.msg);
    }

    const imageArray = () => {
        if(itemInfo.image2) {
            return [{url: itemInfo.itemImage}, {url: itemInfo.image1}, {url: itemInfo.image2}];
        } else {
            if(itemInfo.image1) {
                return [{url: itemInfo.itemImage}, {url: itemInfo.image1}];
            } else {
                if(itemInfo.itemImage) {
                    return [{url: itemInfo.itemImage}];
                } else {
                    return [];
                }
            }
        }
    }

    const images = imageArray().map((image, index) => (
        <Swiper.Item key={index}>
            <div className='imagesContainer'  onClick={()=>{ setModifyVisible(true) }}>
                <Image src={image.url} />
            </div>
        </Swiper.Item>
    ))

    const rightActions = [
      {
        key: 'delete',
        text: '??????',
        color: 'danger',
        onClick: async ()=>{
            props.setMaskVisible(true);
            if(new Date(props.time[1]) > new Date()) {
                let response = await doGet("/deleteItemByID/" + items[index].itemId);
                if(response.status == 0) {
                    let itemList = items;
                    itemList.splice(index, 1);
                    console.log(itemList);
                    props.setItems([...itemList]);
                    props.setMaskVisible(false);
                    Toast.show("????????????");
                } else {
                    props.setMaskVisible(false);
                    Toast.show("????????????");
                }
            } else {
                props.setMaskVisible(false);
                Toast.show("???????????????, ????????????");
            }
        }
      },
    ]

    return  (
      <SwipeAction
        key={itemInfo.itemName}
        rightActions={rightActions}
      >
          <List.Item
            width='100%'
            style={{flex: 1}}
            prefix={
                <Badge
                    color='#87d068'
                    content={index + 1}
                    style={{ '--right': '100%', '--top': '10%' }}
                >
                    {
                        (!itemInfo.image1)?
                        <div className='imagesContainer'  onClick={()=>{ (new Date(props.time[1]) > new Date())? setModifyVisible(true) : Toast.show('???????????????, ????????????') }}>
                            <Image src={itemInfo.itemImage} />
                        </div> : 
                        <div className='imagesContainer'  onClick={()=>{ (new Date(props.time[1]) > new Date())? setModifyVisible(true) : Toast.show('???????????????, ????????????') }}>
                        <Swiper loop autoplay style={{'--width': '100px', '--height': '100px'}}>
                            { images }
                        </Swiper>
                        </div>
                    }
                </Badge>
            }
            description={
                <div>
                    <Ellipsis direction='end' rows={2} style={{fontSize: 12}} content={itemInfo.itemDescription? itemInfo.itemDescription : '????????????'} />
                </div>
            }
            extra={
                <>
                <div style={{width: '100%', fontSize: 20, fontWeight: 700, color: 'red'}}>
                    ???{IntToPrice(itemInfo.itemPrice)}
                </div>
                <div>??????{itemInfo.itemStock}???</div>
                </>
            }
            arrow={false}
          >
              {itemInfo.itemName}
              {
                itemInfo.itemSeckill? <>
                <Popover
                    content={<div>
                      <div style={{fontSize: 7, color: 'green'}}>
                        {ToDisplay(itemInfo.skStartTime)}
                      </div>
                      <div style={{fontSize: 7, color: 'red'}}>
                        {ToDisplay(itemInfo.skEndTime)}
                      </div>
                    </div>}
                    trigger='click'
                    placement='left'
                >
                  <Tag color='danger' style={{marginLeft: '5px'}}>????????????</Tag>
                </Popover>
                {
                    new Date() < new Date(itemInfo.skStartTime)?
                    <div style={{ height: 20, marginTop: 2 }}>
                        <Countdown style={{ float: 'left' }} valueStyle={{ fontSize:6 }} value={new Date(itemInfo.skStartTime)}  format="H???m???s???" />
                        <span style={{ float: 'left', marginLeft: 2, fontSize: 6 }}>?????????</span>
                    </div> : 
                    new Date() < new Date(itemInfo.skEndTime)?
                    <div style={{ height: 20, marginTop: 2 }}>
                        <Countdown style={{ float: 'left' }} valueStyle={{ fontSize:6 }} value={new Date(itemInfo.skEndTime)}  format="H???m???s???" />
                        <span style={{ float: 'left', marginLeft: 2, fontSize: 6 }}>?????????</span>
                    </div> : 
                    <div style={{ float: 'left', marginLeft: 1, fontSize: 6 }}>???????????????</div>
                } </>
                :<></>
              }
          </List.Item>

          <ModifyItemPopup visible={modifyVisible} setVisible={setModifyVisible} onFinish={modifyItem} item={itemInfo} time={props.time} />
      </SwipeAction>
    );
}

const ModifyItemPopup = (props) => {
    const [form] = Form.useForm();
    const visible = props.visible;
    const setVisible = props.setVisible;
    const [secKillStartPickerVisible, setSecKillStartPickerVisible] = useState(false);
    const [secKillEndPickerVisible, setSecKillEndPickerVisible] = useState(false);
    const [secKill, setSecKill] = useState(props.item.itemSeckill);

    const onFinish = (values) => {
        setVisible(false);
        form.setFieldsValue(values);
        values["skStartTime"] = ToYYYYMMDD(values.skStartTime);
        values["skEndTime"] = ToYYYYMMDD(values.skEndTime);
        console.log(values);
        props.onFinish(values);
    }

    const imageArray = () => {
        if(props.item.image2) {
            return [{url: props.item.itemImage}, {url: props.item.image1}, {url: props.item.image2}];
        } else {
            if(props.item.image1) {
                return [{url: props.item.itemImage}, {url: props.item.image1}];
            } else {
                if(props.item.itemImage) {
                    return [{url: props.item.itemImage}];
                } else {
                    return [];
                }
            }
        }
    }

    return (
        <Popup visible={visible} onMaskClick={()=>{setVisible(false)}}>
            <div style={{ height: '80vh', overflowY: 'scroll' }}>
                <Form
                    layout='horizontal'
                    onFinish={(values)=>{ onFinish(values) }}
                    footer={ <Button block type='submit' color='primary' size='large'>??????</Button> }
                    form={form}
                >
                    <Form.Header>?????????????????????</Form.Header>

                    <Form.Item name='itemName' label='????????????' rules={[{ required: true, message: '????????????????????????' }]} initialValue={props.item.itemName}>
                        <Input placeholder='?????????????????????' />
                    </Form.Item>

                    <Form.Item name='itemStock' label='????????????' rules={[{ required: true, message: '????????????????????????' }]} initialValue={props.item.itemStock}>
                        <Stepper min={1} />
                    </Form.Item>

                    <Form.Item name='itemPrice' label='????????????' initialValue={IntToPrice(props.item.itemPrice)} rules={[
                        {
                            required: true,
                            pattern: new RegExp(/^(([1-9]\d*)|\d)(\.\d{1,2})?$/, 'g'),
                            message: '????????????????????????',
                        },
                    ]}>
                        <Input placeholder='?????????????????????' />
                    </Form.Item>

                    <Form.Item name='itemSeckill' label='????????????' initialValue={props.item.itemSeckill}>
                        <Checkbox style={{'--icon-size': '18px'}} onChange={v=>{ setSecKill(v) }} defaultChecked={props.item.itemSeckill}>??????</Checkbox>
                    </Form.Item>

                    {
                        (secKill)?
                        <>
                            <Form.Item name='skStartTime' label='????????????' trigger='onConfirm' onClick={() => { setSecKillStartPickerVisible(true) }} 
                                initialValue={new Date(props.item.skStartTime)}
                                rules={[{required: true, message: '???????????????????????????'}, { validator: (rule, value=null)=>{
                                    const endTime = form.getFieldValue('skEndTime');  
                                    if(endTime && value && value > endTime)   return new Promise((resolve, reject) => reject('????????????????????????????????????'));
                                    return Promise.resolve();
                                }}]} dependencies={['skEndTime']}>
                                <DatePicker visible={secKillStartPickerVisible} precision='second' onClose={() => { setSecKillStartPickerVisible(false) }} min={new Date(props.time[0])} max={new Date(props.time[1])}>
                                    {
                                        value =>value ? ToYYYYMMDD(value) : '???????????????????????????'
                                    }
                                </DatePicker>
                            </Form.Item>
                    
                            <Form.Item name='skEndTime' label='????????????' trigger='onConfirm' onClick={() => { setSecKillEndPickerVisible(true) }}
                                initialValue={new Date(props.item.skEndTime)}
                                rules={[{required: true, message: '???????????????????????????'}, { validator: (rule, value=null)=>{
                                    const startTime = form.getFieldValue('skStartTime');
                                    if(startTime && value && value < startTime)   return new Promise((resolve, reject) => reject('????????????????????????????????????'));
                                    return Promise.resolve();
                                  }}]} dependencies={['skStartTime']}>
                                <DatePicker visible={secKillEndPickerVisible} precision='second' onClose={() => { setSecKillEndPickerVisible(false) }} min={new Date(props.time[0])} max={new Date(props.time[1])}>
                                    {
                                        value =>value ? ToYYYYMMDD(value) : '???????????????????????????'
                                    }
                                </DatePicker>
                            </Form.Item>
                        </> : <></>
                    }
                    <Form.Item name='itemImage' label='????????????' initialValue={imageArray()}>
                        <ImageUploader
                            upload={imageUpload}
                        />
                    </Form.Item>
                    <Form.Item name='itemDescription' label='????????????' help='?????????300???' initialValue={props.item.itemDescription}>
                        <TextArea placeholder='?????????????????????' maxLength={300} autoSize={{minRows: 4, maxRows: 12}} />
                    </Form.Item>
                </Form>
            </div>
        </Popup>
      );
}

export default ProductList;