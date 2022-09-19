import React, { useState } from 'react'
import {
  Form,
  Input,
  Button,
  TextArea,
  DatePicker,
  ImageUploader,
  Stepper,
  Popup,
  Checkbox,
} from 'antd-mobile'
import { imageUpload } from '../utils/imageUpload'
import dayjs from 'dayjs'

export default (props) => {
    const [form] = Form.useForm();
    const visible = props.visible;
    const setVisible = props.setVisible;
    const append = props.append;
    const [secKillStartPickerVisible, setSecKillStartPickerVisible] = useState(false);
    const [secKillEndPickerVisible, setSecKillEndPickerVisible] = useState(false);
    const [secKill, setSecKill] = useState(false);

    return (
        <Popup visible={visible} onMaskClick={()=>{setVisible(false)}}>
            <div style={{ height: '80vh', overflowY: 'scroll' }}>
                <Form
                    layout='horizontal'
                    onFinish={(values)=>{ 
                        setVisible(false);
                        props.onFinish(values);
                        setSecKill(false);
                        form.resetFields();
                    }}
                    footer={
                        <Button block type='submit' color='primary' size='large'>
                            添加
                        </Button>
                    }
                    form={form}
                >
                    <Form.Header>请输入商品信息</Form.Header>

                    <Form.Item name='itemName' label='商品名称' rules={[{ required: true, message: '商品名称不能为空' }]}>
                        <Input placeholder='请输入商品名称' />
                    </Form.Item>

                    <Form.Item name='itemStock' label='商品库存' rules={[{ required: true, message: '商品库存不能为空' }]}>
                        <Stepper min={1} />
                    </Form.Item>

                    <Form.Item name='itemPrice' label='商品价格' rules={[
                        {
                            required: true,
                            pattern: new RegExp(/^(([1-9]\d*)|\d)(\.\d{1,2})?$/, 'g'),
                            message: '请输入正确的金额',
                        },
                    ]}>
                        <Input placeholder='请输入商品价格' />
                    </Form.Item>

                    <Form.Item name='itemSeckill' label='商品秒杀' initialValue={false}>
                        <Checkbox style={{'--icon-size': '18px'}} onChange={v=>{ setSecKill(v) }} defaultChecked={false}>秒杀</Checkbox>
                    </Form.Item>

                    {
                        secKill && (append?
                        <>
                            <Form.Item name='skStartTime' label='开始时间' trigger='onConfirm' onClick={() => { setSecKillStartPickerVisible(true) }} 
                                rules={[{required: true, message: '请选择秒杀开始时间'}, { validator: (rule, value=null)=>{
                                    const endTime = form.getFieldValue('skEndTime');  
                                    if(endTime && value && value > endTime)   return new Promise((resolve, reject) => reject('开始时间不能晚于结束时间'));
                                    return Promise.resolve();
                                }}, { validator: (rule, value=null)=>{
                                    if(value && value < props.startTime)   return new Promise((resolve, reject) => reject('秒杀开始时间不能早于团购开始时间'));
                                    return Promise.resolve();
                                }}]} dependencies={['skEndTime']}>
                                <DatePicker visible={secKillStartPickerVisible} precision='second' onClose={() => { setSecKillStartPickerVisible(false) }} min={new Date()}>
                                    {
                                        value =>value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '请选择秒杀开始时间'
                                    }
                                </DatePicker>
                            </Form.Item>
                    
                            <Form.Item name='skEndTime' label='截止时间' trigger='onConfirm' onClick={() => { setSecKillEndPickerVisible(true) }}
                                rules={[{required: true, message: '请选择秒杀截止时间'}, { validator: (rule, value=null)=>{
                                    const startTime = form.getFieldValue('skStartTime');
                                    if(startTime && value && value < startTime)   return new Promise((resolve, reject) => reject('结束时间不能早于开始时间'));
                                    return Promise.resolve();
                                  }}, { validator: (rule, value=null)=>{
                                        if(value && value > props.endTime)   return new Promise((resolve, reject) => reject('秒杀结束时间不能晚于团购结束时间'));
                                        return Promise.resolve();
                                  }}]} dependencies={['skStartTime']}>
                                <DatePicker visible={secKillEndPickerVisible} precision='second' onClose={() => { setSecKillEndPickerVisible(false) }} min={new Date()}>
                                    {
                                        value =>value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '请选择秒杀截止时间'
                                    }
                                </DatePicker>
                            </Form.Item>
                        </> : 
                        <>
                            <Form.Item name='skStartTime' label='开始时间' trigger='onConfirm' onClick={() => { setSecKillStartPickerVisible(true) }} 
                                rules={[{required: true, message: '请选择秒杀开始时间'}, { validator: (rule, value=null)=>{
                                    const endTime = form.getFieldValue('skEndTime');  
                                    if(endTime && value && value > endTime)   return new Promise((resolve, reject) => reject('开始时间不能晚于结束时间'));
                                    return Promise.resolve();
                                }}]} dependencies={['skEndTime']}>
                                <DatePicker visible={secKillStartPickerVisible} precision='second' onClose={() => { setSecKillStartPickerVisible(false) }} min={new Date()}>
                                    {
                                        value =>value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '请选择秒杀开始时间'
                                    }
                                </DatePicker>
                            </Form.Item>
                    
                            <Form.Item name='skEndTime' label='截止时间' trigger='onConfirm' onClick={() => { setSecKillEndPickerVisible(true) }}
                                rules={[{required: true, message: '请选择秒杀截止时间'}, { validator: (rule, value=null)=>{
                                    const startTime = form.getFieldValue('skStartTime');
                                    if(startTime && value && value < startTime)   return new Promise((resolve, reject) => reject('结束时间不能早于开始时间'));
                                    return Promise.resolve();
                                    }}]} dependencies={['skStartTime']}>
                                <DatePicker visible={secKillEndPickerVisible} precision='second' onClose={() => { setSecKillEndPickerVisible(false) }} min={new Date()}>
                                    {
                                        value =>value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '请选择秒杀截止时间'
                                    }
                                </DatePicker>
                            </Form.Item>
                        </>)
                    }
                    <Form.Item name='itemImage' label='商品图片'>
                        <ImageUploader
                            upload={imageUpload}
                            maxCount={3}
                        />
                    </Form.Item>
                    <Form.Item name='itemDescription' label='商品描述' help='不超过300字'>
                        <TextArea placeholder='请输入商品描述' maxLength={300} autoSize={{minRows: 4, maxRows: 12}} />
                    </Form.Item>
                </Form>
            </div>
        </Popup>
    );
}