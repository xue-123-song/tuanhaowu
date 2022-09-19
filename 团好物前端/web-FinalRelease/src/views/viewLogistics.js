import { List, Steps, Space, Image, Modal, Form, Input, TextArea, Toast } from 'antd-mobile';
import { TruckOutline, CheckCircleOutline, ClockCircleOutline, HandPayCircleOutline, EditSOutline } from 'antd-mobile-icons';
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Header from '../components/header';
import { Button } from 'antd';
import { IntToPrice } from '../utils/transformPrice';
import { doGet, doJSONPost } from '../utils/ajax';

const { Step } = Steps;

export default () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [info, setInfo] = useState(location.state.info);
    const [form] = Form.useForm();
    const logisticsWay = ['快递', '同城配送', '顾客自提', '无物流'];
    const [current, setCurrent] = useState(() =>{
            switch(info.orderStatus) {
                case 0: return 1;
                case 1: return 3;
                case 2: return 5;
                case 3: return 7;
                default: return 0;
            }
        }
    );

    // const goBack = () => {
    //     console.log("回退");
    //     navigate('/order', { replace: true });
    // }

    // useEffect(()=>{
    //     window.history.pushState(null, null);
    //     window.addEventListener('popstate', goBack);
    //     console.log("监听");
    //     return () => {console.log("移除"); window.removeEventListener('popstate', goBack) };
    // }, []);

    const text = (txt) => {
        return <div style={{ fontSize: 11, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{txt}</div>
    }

    const editInfo = () => {
        Modal.alert({
            confirmText: '修改',
            closeOnMaskClick: true,
            content: (
                <Form
                    layout='horizontal'
                    form={form}
                >
                    <Form.Item name='receiverName' label={text('收货人')} style={{ '--prefix-width': '64px' }} initialValue={"团购爱好者"}>
                        <Input placeholder='请输入收货人' style={{ '--font-size': '12px' }} />
                    </Form.Item>
                    <Form.Item name='tel' label={text('联系电话')} initialValue={info.tel} style={{ '--prefix-width': '64px' }}>
                        <Input placeholder='请输入电话' style={{ '--font-size': '12px' }} />
                    </Form.Item>
                    <Form.Item name='address' label={text('收货地址')} initialValue={info.address} style={{ '--prefix-width': '64px' }}>
                        <TextArea placeholder='请输入收获地址' maxLength={300} autoSize={{minRows: 1, maxRows: 12}} style={{ '--font-size': '12px' }} />
                    </Form.Item>
                </Form>
            ),
            onConfirm: async ()=>{
                let values = form.getFieldsValue();
                let tmp = {...info};
                tmp['receiverName'] = values.receiverName;
                tmp['tel'] = values.tel;
                tmp['address'] = values.address;
                values["orderId"] = info.orderId;
                console.log(info);
                let response = await doJSONPost("/modifyOrderInfo", values);
                console.log(response);
                if(response.status == 0) {
                    console.log(tmp);
                    setInfo(tmp);
                }
                Toast.show(response.msg);
            }
        })
    }

    const deliver = () => {
        const toDo = async() => {
            let response = await doGet("/deliverOrder/" + info.orderId);
            setCurrent(5);
            Toast.show("发货成功");
        }
        toDo();
    }

    return (
        <div style={{ flex: 1 }}>
            <Header title='物流详情' onBack={ () => { window.history.back(); }} />

            <Space direction='vertical' block>
            <List.Item
                prefix={<div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%' }}><TruckOutline fontSize={36} /></div>}
                title={ <>张三<span> { info.tel } </span> <span onClick={()=>editInfo()}><EditSOutline /></span></> }
                style={{ '--prefix-width': '60px' }}
            >
                { info.address }
            </List.Item>

            <List.Item>
                <div style={{ paddingLeft: '16px', paddingRight: '16px' }}>订单编号: { info.orderId } </div>
                <div style={{ 'marginLeft': '16px' }}>快递单号: { info.expressId? info.expressId : '暂无' }</div>
                <div style={{ 'marginLeft': '16px' }}>物流方式: 快递</div>
            </List.Item>
            <List.Item>
                <Steps
                direction='vertical'
                current={current}
                style={{
                    '--title-font-size': '14px',
                    '--description-font-size': '12px',
                    '--indicator-margin-right': '12px',
                    '--icon-size': '16px',
                }}
                >
                <Step
                    title='用户下单'
                    description='2022年9月11日'
                    icon={<CheckCircleOutline />}
                />
                <Step
                    title='等待用户付款'
                    icon={<ClockCircleOutline />}
                />
                {
                    current > 1 &&
                    <Step
                        title='用户付款成功'
                        description='2022年9月11日'
                        icon={current > 2 && <CheckCircleOutline />}
                    />
                }
                {
                    current > 2 &&
                    <Step
                        title={
                            <Space block direction='horizontal'>
                                等待团长发货
                                { current == 3 && <Button className='adm-button3' color='primary' size='small' onClick={()=>deliver()}>发货</Button> }
                            </Space>
                        }
                        icon={<ClockCircleOutline />}
                    />
                }
                {
                    current > 3 &&
                    <Step
                        title='团长已发货'
                        description='2022年9月11日'
                        icon={current > 4 && <CheckCircleOutline />}
                    />
                }
                {
                    current > 4 &&
                    <Step
                        title='等待团员收货'
                        icon={<ClockCircleOutline />}
                    />
                }
                {
                    current > 5 &&
                    <Step
                        title='团员已收货'
                        description='2022年9月11日'
                        icon={current > 6 && <CheckCircleOutline />}
                    />
                }
                </Steps>
            </List.Item>
            </Space>

        </div>
    );
}
