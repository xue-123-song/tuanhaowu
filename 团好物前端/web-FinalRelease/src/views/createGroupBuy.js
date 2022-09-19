import React, { useState } from 'react'
import Header from '../components/header'
import LoadingMask from '../components/loadingMask'
import ProductItem from '../components/productItem'
import AddItemPopup from '../components/addItemPopup'
import { AddOutline } from 'antd-mobile-icons'
import { ToYYYYMMDD } from '../utils/transformTime'
import { PriceToInt } from '../utils/transformPrice'
import { createGroupBuy } from '../services/groupService'
import { Form, Input, Button, TextArea, DatePicker, Space, Toast, Selector } from 'antd-mobile'
import { useNavigate } from 'react-router-dom'

export default () => {
    const navigate = useNavigate();
    const [items, setItems] = useState([])
    const [groupBuyForm] = Form.useForm();
    const [maskVisible, setMaskVisible] = useState(false);
    const [popupVisible, setPopupVisible] = useState(false);
    const [endPickerVisible, setEndPickerVisible] = useState(false);
    const [startPickerVisible, setStartPickerVisible] = useState(false);
    const logisticOptions = [
        {  
            label: '快递',
            value: 1, 
        },
        {   label: '同城配送',
            value: 2, 
        },
        {   label: '顾客自提',
            value: 3, 
        },
        {   label: '无物流',
            value: 4, 
        },
    ]

    const addItem = (newItem) => {
        let itemList = items;
        newItem["itemPrice"] = PriceToInt(newItem["itemPrice"]);
        itemList.push(newItem);
        setItems(itemList);
    }

    const modifyItem = (itemList) => {
        setItems(itemList);
    }

    const onFinish = async (groupInfo) => {
        setMaskVisible(true);
        let responseJSON = await createGroupBuy(groupInfo, items);
        setMaskVisible(false);
        if(responseJSON != null) {
            Toast.show({
                content: responseJSON.msg,
                afterClose: () => {
                    window.history.back();
                },
                icon: 'success'
            });
        }
    }

    return (
        <div style={{flex: 1}}>
            <Header onBack={() => { navigate(-1) }} title='新建团购' />
            <Form footer={<Button block type='submit' color='primary' size='large'>提交</Button>}
                  mode='card' layout="horizontal" onFinish={v=>onFinish(v)}  form={groupBuyForm}
            >
                <Form.Header>新建团购</Form.Header>
                <Form.Item name='groupTitle' label='标题' rules={[{ required: true, message: '标题不能为空' }]}>
                  <Input placeholder='请输入团购活动标题' />
                </Form.Item>
                <Form.Item name='groupDescription' label='描述'>
                  <TextArea placeholder='请输入团购活动内容' autoSize={{minRows: 3, maxRows: 8}} />
                </Form.Item>
                <Form.Item name='startTime' label='开始时间' trigger='onConfirm' onClick={() => setStartPickerVisible(true)}
                    rules={[{required: true, message: '请选择开始时间'}, { validator: (rule, value=null)=>{
                        const endTime = groupBuyForm.getFieldValue('endTime');  
                        if(endTime && value > endTime)   
                            return new Promise((_, reject) => reject('开始时间不能晚于结束时间'));
                        return Promise.resolve();
                    }}]} 
                    dependencies={['endTime']}
                >
                    <DatePicker visible={startPickerVisible} precision='second' onClose={() => setStartPickerVisible(false)} min={new Date()}>
                        { 
                            value => value ? ToYYYYMMDD(value) : '请选择时间'
                        }
                    </DatePicker>
                </Form.Item>

                <Form.Item name='endTime' label='截止时间' trigger='onConfirm' onClick={() => setEndPickerVisible(true)}
                    rules={[{required: true, message: '请选择截止时间'}, { validator: (rule, value=null)=>{
                      const startTime = groupBuyForm.getFieldValue('startTime');
                      if(startTime && value < startTime)   return new Promise((resolve, reject) => reject('结束时间不能早于开始时间'));
                      return Promise.resolve();
                    }}]} dependencies={['startTime']}
                >
                    <DatePicker visible={endPickerVisible} precision='second' onClose={() => { setEndPickerVisible(false) }} min={new Date()}>
                      {
                        value => value ? ToYYYYMMDD(value) : '请选择时间'
                      }
                    </DatePicker>
                </Form.Item>
                <Form.Item name='logistics' label='物流方式' trigger='onConfirm' rules={[{required: true, message: '请选择物流方式'}]}>
                    <Selector
                      options={logisticOptions}
                      onChange={(v)=>{groupBuyForm.setFieldsValue({logistics: v})}}
                    />
                </Form.Item>
                {
                    items.map((_, index)=>{
                        return <ProductItem key={index} index={index} items={items} setItems={modifyItem} />
                    })
                }
                <Button block type='button' onClick={()=>{setPopupVisible(true)}}>
                    <Space>
                        <AddOutline />
                        <span>添加商品</span>
                    </Space>
                </Button>
            </Form>
            <AddItemPopup visible={popupVisible} setVisible={setPopupVisible} onFinish={addItem} append={false} />    
            <LoadingMask visible={maskVisible} />
        </div>
    )
}