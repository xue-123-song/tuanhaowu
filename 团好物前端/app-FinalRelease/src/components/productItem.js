import React, { useState } from 'react';
import { SwipeAction, Button, Image, Form, Input, Tag, List, Badge, Ellipsis, Popup, Stepper, Popover, TextArea, ImageUploader, DatePicker, Checkbox } from 'antd-mobile';
import { ShopbagOutline } from 'antd-mobile-icons';
import { PriceToInt, IntToPrice } from '../utils/transformPrice';
import { ToDisplay, ToYYYYMMDD } from '../utils/transformTime';
import { imageUpload } from '../utils/imageUpload';

const ProductItem = (props) => {
    const index = props.index;
    const items = props.items;
    const itemInfo = items[index];
    const setItems = props.setItems;
    const [modifyVisible, setModifyVisible] = useState(false);

    const modifyItem = (item) => {
        let itemList = items;
        item["itemPrice"] = PriceToInt(item["itemPrice"]);
        itemList[index] = item;
        setItems(itemList);
    }

    const rightActions = [
      {
        key: 'delete',
        text: '删除',
        color: 'danger',
        onClick: ()=>{           
          let itemList = items;
          itemList.splice(index, 1);
          console.log(itemList);
          setItems([...itemList]); 
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
          prefix={<Badge
            color='#87d068'
            content={index + 1}
            style={{ '--right': '100%', '--top': '10%' }}
          >
          <div className='imagesContainer'  onClick={()=>{ setModifyVisible(true) }}><Image src={itemInfo.itemImage.length && itemInfo.itemImage[0].url} /></div>
          </Badge>}
          description={<Ellipsis direction='end' rows={2} style={{fontSize: 12}} content={itemInfo.itemDescription? itemInfo.itemDescription : '暂无描述'} />}
          extra={
              <>
              <div style={{width: '100%', fontSize: 20, fontWeight: 700, color: 'red'}}>
                  ￥{IntToPrice(itemInfo.itemPrice)}
              </div>
              <div>库存{itemInfo.itemStock}件</div>
              </>
          }
          arrow={false}>
              {itemInfo.itemName}
              {
                itemInfo.itemSeckill && 
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
                  <Tag color='danger' style={{marginLeft: '5px'}}>秒杀商品</Tag>
                </Popover>
              }
          </List.Item>

          <ModifyItemPopup visible={modifyVisible} setVisible={setModifyVisible} onFinish={modifyItem} item={itemInfo} />
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
        props.onFinish(values);
    }

    return (
        <Popup visible={visible} onMaskClick={()=>{setVisible(false)}}>
            <div style={{ height: '80vh', overflowY: 'scroll' }}>
                <Form
                    layout='horizontal'
                    onFinish={(values)=>{ onFinish(values) }}
                    footer={
                        <>
                            <Button block type='submit' color='primary' size='large'>修改</Button>
                        </>
                    }
                    form={form}
                >
                    <Form.Header>请输入商品信息</Form.Header>

                    <Form.Item name='itemName' label='商品名称' rules={[{ required: true, message: '商品名称不能为空' }]} initialValue={props.item.itemName}>
                        <Input placeholder='请输入商品名称' />
                    </Form.Item>

                    <Form.Item name='itemStock' label='商品库存' rules={[{ required: true, message: '商品库存不能为空' }]} initialValue={props.item.itemStock}>
                        <Stepper min={1} />
                    </Form.Item>

                    <Form.Item name='itemPrice' label='商品价格' initialValue={IntToPrice(props.item.itemPrice)} rules={[
                        {
                            required: true,
                            pattern: new RegExp(/^(([1-9]\d*)|\d)(\.\d{1,2})?$/, 'g'),
                            message: '请输入正确的金额',
                        },
                    ]}>
                        <Input placeholder='请输入商品价格' />
                    </Form.Item>

                    <Form.Item name='itemSeckill' label='商品秒杀' initialValue={props.item.itemSeckill}>
                        <Checkbox style={{'--icon-size': '18px'}} onChange={v=>{ setSecKill(v) }} defaultChecked={props.item.itemSeckill}>秒杀</Checkbox>
                    </Form.Item>

                    {
                        (secKill)?
                        <>
                            <Form.Item name='skStartTime' label='开始时间' trigger='onConfirm' onClick={() => { setSecKillStartPickerVisible(true) }} 
                                initialValue={props.item.skStartTime}
                                rules={[{required: true, message: '请选择秒杀开始时间'}, { validator: (rule, value=null)=>{
                                    const endTime = form.getFieldValue('skEndTime');  
                                    if(endTime && value && value > endTime)   return new Promise((resolve, reject) => reject('开始时间不能晚于结束时间'));
                                    return Promise.resolve();
                                }}]} dependencies={['skEndTime']}>
                                <DatePicker visible={secKillStartPickerVisible} precision='second' onClose={() => { setSecKillStartPickerVisible(false) }} min={new Date()}>
                                    {
                                        value =>value ? ToYYYYMMDD(value) : '请选择秒杀开始时间'
                                    }
                                </DatePicker>
                            </Form.Item>
                    
                            <Form.Item name='skEndTime' label='截止时间' trigger='onConfirm' onClick={() => { setSecKillEndPickerVisible(true) }}
                                initialValue={props.item.skEndTime}
                                rules={[{required: true, message: '请选择秒杀截止时间'}, { validator: (rule, value=null)=>{
                                    const startTime = form.getFieldValue('skStartTime');
                                    if(startTime && value && value < startTime)   return new Promise((resolve, reject) => reject('结束时间不能早于开始时间'));
                                    return Promise.resolve();
                                  }}]} dependencies={['skStartTime']}>
                                <DatePicker visible={secKillEndPickerVisible} precision='second' onClose={() => { setSecKillEndPickerVisible(false) }} min={new Date()}>
                                    {
                                        value =>value ? ToYYYYMMDD(value) : '请选择秒杀截止时间'
                                    }
                                </DatePicker>
                            </Form.Item>
                        </> : <></>
                    }
                    <Form.Item name='itemImage' label='商品图片' initialValue={props.item.itemImage}>
                        <ImageUploader
                            upload={imageUpload}
                        />
                    </Form.Item>
                    <Form.Item name='itemDescription' label='商品描述' help='不超过300字' initialValue={props.item.itemDescription}>
                        <TextArea placeholder='请输入商品描述' maxLength={300} autoSize={{minRows: 4, maxRows: 12}} />
                    </Form.Item>
                </Form>
            </div>
        </Popup>
      );
}

export default ProductItem;