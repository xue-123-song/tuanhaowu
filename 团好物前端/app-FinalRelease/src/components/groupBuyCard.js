import React, { useState } from 'react';
import { Modal, Menu, Dropdown } from 'antd';
import { Button, Form, Input, TextArea, DatePicker, Selector, Tag, Image, List, Dialog, Toast } from 'antd-mobile';
import { MoreOutline, SystemQRcodeOutline, EditSFill, ClockCircleFill, CloseCircleFill, DeleteOutline, SendOutline } from 'antd-mobile-icons';
import { useNavigate } from 'react-router-dom';
import ShowLinkAndQR from '../views/showLinkAndQR';
import { doGet, doJSONPost } from '../utils/ajax';
import { ToYYYYMMDD } from '../utils/transformTime';
import 'antd/dist/antd.css';
import LoadingMask from './loadingMask';
import plusShare from '../utils/plusShare';

export default (props) => {
    const navigate = useNavigate();
    const [qrVisible, setQRVisible] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [maskVisible, setMaskVisible] = useState(false);

    const modifyGroupBuy = async (values) => {
        values["groupId"] = props.info.groupId;
        values["groupLeader"] = props.info.groupLeader;
        let [logisticWay] = values["logistics"];
        values["logistics"] = logisticWay;
        values["startTime"] = ToYYYYMMDD(values["startTime"]);
        values["endTime"] = ToYYYYMMDD(values["endTime"]);
        setMaskVisible(true);

        console.log(values);

        let response = await doJSONPost("/changeGroupInfo", values);

        setMaskVisible(false);
        props.setGroupBuyList(response.value);
        console.log("response " + JSON.stringify(response));
        if(response.key.status == 0) {
            Toast.show({
            content: "修改成功",
            icon: 'success',
            });
        }
    }
    const cancelGroupBuy = () => {
        Dialog.confirm({
            content: '您确定要取消 ' + props.info.groupTitle + " 吗?",
            confirmText: <div style={{color: 'red', fontWeight: 'bold'}}>确定</div>,
            onConfirm: async() => {
                console.log("取消中");
                let response = await doGet("/cancelGroup/" + props.info.groupId);
                let groupInfo = await doGet('/searchGroupByGroupLeader/' + props.info.groupLeader);
                props.setGroupBuyList(groupInfo);
                if(response.status == 1) {
                    Toast.show("取消团购成功");
                } else {
                    Toast.show(response.meg);
                }
            }
        })
    }
    const finishGroup = async() => {
        let info = props.info;
        info["endTime"] = ToYYYYMMDD(new Date());
        let response = await doJSONPost("/changeGroupInfo", info);

        props.setGroupBuyList(response.value);
        console.log("response " + JSON.stringify(response));
        if(response.key.status == 0) {
            Toast.show({
            content: "团购已结束",
            icon: 'success',
            });
        }
    }

    const deleteGroupBuy = async() => {
        Dialog.confirm({
            content: '您确定要删除 ' + props.info.groupTitle + " 吗?",
            confirmText: <div style={{color: 'red', fontWeight: 'bold'}}>确定</div>,
            onConfirm: async() => {
                console.log("删除中");
                let response = await doGet("/deleteGroupByID/" + props.info.groupId);
                if(response.status == 0) {
                    let groupInfo = await doGet('/searchGroupByGroupLeader/' + props.info.groupLeader);
                    props.setGroupBuyList(groupInfo);
                }
                Toast.show(response.msg);
            }
        })
    }

    const shareGroup = () => {
        let content = '团长 ' + props.info.groupLeader + ' 向您分享团购 ' + props.info.groupTitle;
        content = content + ((props.info.groupDescription)? "," + props.info.groupDescription : "") + "。";
        //content = content + "复制链接, 打开团好物APP查看团购。您还可以在手机浏览器中打开链接, 点击打开APP, 进入APP中查看。或者直接点击下方链接打开团购。";
        console.log(content);

        let message = {
            title: props.info.groupTitle,
            content: content,
            href: "http://124.71.160.191/group_info?groupId=" + props.info.groupId,
            thumbs: [props.info.items[0].itemImage]
        }

        plusShare(message, function(res) {
            if(res) {
                window.plus.nativeUI.toast("分享成功");
            } else {
                window.plus.nativeUI.toast("分享失败");
            }
        })
    }

    const menu = (
        <Menu
          style={{borderRadius: 8}}
          items={[
            {
              key: '1',
              label: <span style={{fontSize: 12, marginRight: 10}}>修改团购</span>,
              icon: <EditSFill fontSize={48} color='blue' />,
              onClick: ()=>setModalVisible(true)
            },
            {
              key: '2',
              label: <div style={{fontSize: 12, marginRight: 10}}>提前结束</div>,
              icon: <ClockCircleFill fontSize={24} color='green' />,
              disabled: new Date(props.info.endTime) < new Date(),
              onClick: ()=>finishGroup(),
            },
            {
              key: '3',
              label: <div style={{fontSize: 12, marginRight: 10}}>取消团购</div>,
              icon: <CloseCircleFill fontSize={24} color='red' />,
              onClick: ()=>cancelGroupBuy(),
            },
            {
              key: '4',
              label: <div style={{fontSize: 12, marginRight: 10}}>删除记录</div>,
              icon: <DeleteOutline fontSize={24} />,
              disabled: new Date(props.info.endTime) > new Date(),
              onClick: ()=>deleteGroupBuy(),
            }
          ]}
        />
    );

    return (
        <List.Item title={
            <div style={{ fontWeight: 'bold', paddingLeft: 10 }}>
                {props.info.groupTitle}
                {
                    (!props.info.status)?
                    <Tag color='default' style={{marginLeft: '5px'}}>已取消</Tag> :
                    (new Date(props.info.startTime) > new Date())?
                    <Tag color='primary' style={{marginLeft: '5px'}}>未开始</Tag> :
                    (new Date(props.info.endTime) > new Date())?
                    <Tag color='success' style={{marginLeft: '5px'}}>进行中</Tag> :
                    <Tag color='danger' style={{marginLeft: '5px'}}>已结束</Tag>
                }
                <span style={{float: 'right'}}>
                    <SystemQRcodeOutline fontSize={18} style={{marginRight: 10}} onClick={()=>setQRVisible(true)} />
                    <SendOutline fontSize={18} style={{marginRight: 10}} onClick={()=>shareGroup()}  />
                    <Dropdown overlay={menu}>
                        <a onClick={(e) => e.preventDefault()}>
                            <MoreOutline fontSize={18} style={{marginRight: 10}} />
                        </a>
                    </Dropdown>
                </span>
                <div style={{marginRight: 10, height: '8px', borderBottomWidth: 1, borderBottomColor: '#ededed', borderBottomStyle: 'solid' }}></div>
            </div>
        } style={{borderRadius: 10}}>
            <span style={{display: 'inline-flex', backgroundColor: '#fffffe', marginLeft: '3%', width: '94%', paddingLeft: '3%', paddingRight: '5%', paddingTop: 10, paddingBottom: 10}} onClick={()=>{navigate('/view', { replace: false, state: { info: props.info, index: props.index }})}}>
                { props.info.items.length > 0 && props.info.items[0].itemImage && <Image width='30%' style={{marginLeft: '2%', borderRadius: 6}} src={props.info.items[0].itemImage} /> }
                { props.info.items.length > 1 && props.info.items[1].itemImage && <Image width='30%' style={{marginLeft: '2%', borderRadius: 6}} src={props.info.items[1].itemImage} /> }
                { props.info.items.length > 2 && props.info.items[2].itemImage && <Image width='30%' style={{marginLeft: '2%', borderRadius: 6}} src={props.info.items[2].itemImage} /> }
            </span>
            <Modal
                title="链接和二维码"
                visible={qrVisible}
                okText="确认"
                cancelText="取消"
                onCancel={() => { setQRVisible(false); }}
                onOk={() => { setQRVisible(false); }}
                footer={()=>{}}
            >
                <ShowLinkAndQR groupId={props.info.groupId} />
            </Modal>
            <Modal
                title="修改团购信息"
                visible={modalVisible}
                okText="确认"
                cancelText="取消"
                onCancel={() => { setModalVisible(false); }}
                onOk={() => { setModalVisible(false); }}
                footer={()=>{}}
            >
                <ModifyGroupForm info={props.info} setVisible={setModalVisible} onFinish={modifyGroupBuy} />
            </Modal>

            <LoadingMask visible={maskVisible} />
        </List.Item>
    );
}

const ModifyGroupForm = (props) => {
    const [groupBuyForm] = Form.useForm();
    const [startPickerVisible, setStartPickerVisible] = useState(false);
    const [endPickerVisible, setEndPickerVisible] = useState(false);
    const logisticOptions = [
    {
        label: '快递',
        value: 1,
    },
    {
        label: '同城配送',
        value: 2,
    },
    {
        label: '顾客自提',
        value: 3,
    },
    {
        label: '无物流',
        value: 4,
    },
    ]

    return (
        <Form layout="horizontal" onFinish={(values)=>{ props.setVisible(false); props.onFinish(values); }}
            footer={
                <Button block type='submit' color='primary' size='large'>
                    修改
                </Button>
            }
            mode='card'
            form={groupBuyForm}
            style={{width: '100%'}}
        >
            <Form.Header>修改团购信息</Form.Header>

            <Form.Item name='groupTitle' label='标题' initialValue={props.info.groupTitle} rules={[{ required: true, message: '标题不能为空' }]}>
                <Input placeholder='请输入团购活动标题' />
            </Form.Item>

            <Form.Item name='groupDescription' label='描述' initialValue={props.info.groupDescription}>
                <TextArea placeholder='请输入团购活动内容' autoSize={{minRows: 3, maxRows: 8}} />
            </Form.Item>

            <Form.Item name='startTime' label='开始时间' trigger='onConfirm' onClick={() => { setStartPickerVisible(true) }}
            rules={[{required: true, message: '请选择开始时间'}, { validator: (rule, value=null)=>{
                const endTime = groupBuyForm.getFieldValue('endTime');
                if(endTime && value > endTime)   return new Promise((resolve, reject) => reject('开始时间不能晚于结束时间'));
                return Promise.resolve();
            }}]} dependencies={['endTime']} initialValue={new Date(props.info.startTime)}>
                <DatePicker format={'YYYY-MM-DD HH:mm:ss'} visible={startPickerVisible} precision='second' onClose={() => { setStartPickerVisible(false) }} min={new Date()}>
                    {value =>
                    value ? ToYYYYMMDD(value) : '请选择时间'
                    }
                </DatePicker>
            </Form.Item>

            <Form.Item name='endTime' label='截止时间' trigger='onConfirm' onClick={() => { setEndPickerVisible(true) }}
            rules={[{required: true, message: '请选择截止时间'}, { validator: (rule, value=null)=>{
            const startTime = groupBuyForm.getFieldValue('startTime');
            if(startTime && value < startTime)   return new Promise((resolve, reject) => reject('结束时间不能早于开始时间'));
            return Promise.resolve();
            }}]} dependencies={['startTime']} initialValue={new Date(props.info.endTime)}>
                <DatePicker visible={endPickerVisible} precision='second' onClose={() => { setEndPickerVisible(false) }} min={new Date()}>
                    {value =>
                    value ? ToYYYYMMDD(value) : '请选择时间'
                    }
                </DatePicker>
            </Form.Item>

            <Form.Item name='logistics' label='物流方式' initialValue={[props.info.logistics]} trigger='onConfirm' rules={[{required: true, message: '请选择物流方式'}]}>
                <Selector
                    options={logisticOptions}
                    onChange={(v)=>{groupBuyForm.setFieldsValue({logistics: v})}}
                />
            </Form.Item>

        </Form>
    );
}


// export default (props) => {
//     const navigate = useNavigate();
//     const [qrVisible, setQRVisible] = useState(false);
//     const [modalVisible, setModalVisible] = useState(false)
//     const [startPickerVisible, setStartPickerVisible] = useState(false)
//     const [endPickerVisible, setEndPickerVisible] = useState(false)
//     const [groupBuyForm] = Form.useForm();
//     const logisticsWays = ['快递', '同城配送', '顾客自提', '无物流'];
//     const logisticOptions = [
//         {
//           label: '快递',
//           value: 1,
//         },
//         {
//           label: '同城配送',
//           value: 2,
//         },
//         {
//           label: '顾客自提',
//           value: 3,
//         },
//         {
//           label: '无物流',
//           value: 4,
//         },
//     ]

//     const text = (txt) => {
//         return <div style={{fontSize: 14}}>{txt}</div>;
//     }

//     const modifyGroupBuy = async (values) => {
//         values["groupId"] = props.info.groupId;
//         values["groupLeader"] = props.info.groupLeader;
//         let [logisticWay] = values["logistics"];
//         values["logistics"] = logisticWay;
//         values["startTime"] = ToYYYYMMDD(values["startTime"]);
//         values["endTime"] = ToYYYYMMDD(values["endTime"]);

//         let response = await doJSONPost("/changeGroupInfo", values);

//         props.setGroupBuyList(response.value);
//         console.log("response " + JSON.stringify(response));
//         if(response.key.status == 0) {
//             Toast.show({
//             content: "修改成功",
//             icon: 'success',
//             });
//         }
//     }

//     const handleCancel = () => {
//         Dialog.confirm({
//             content: '您确定要取消 ' + props.info.groupTitle + " 吗?",
//             confirmText: <div style={{color: 'red', fontWeight: 'bold'}}>取消</div>,
//             onConfirm: () => deleteGroupBuy(),
//         })
//     }

//     const deleteGroupBuy = async () => {
//         console.log("取消中");
//         let response = await doGet("/cancelGroup/" + props.info.groupId);
//         let groupInfo = await doGet('/searchGroupByGroupLeader/' + props.info.groupLeader);
//         props.setGroupBuyList(groupInfo);
//         if(response.status == 1) {
//             Toast.show("取消团购成功");
//         } else {
//             Toast.show(response.meg);
//         }
//     }

//     const finishGroup = async() => {
//         let info = props.info;
//         info["endTime"] = ToYYYYMMDD(new Date());
//         let response = await doJSONPost("/changeGroupInfo", info);

//         props.setGroupBuyList(response.value);
//         console.log("response " + JSON.stringify(response));
//         if(response.key.status == 0) {
//             Toast.show({
//             content: "团购已结束",
//             icon: 'success',
//             });
//         }
//     }

//     return (
//         <div style={{borderRadius: '36px'}}>
//             <Card
//                 title={
//                     <div style={{ fontWeight: 'bold' }}>
//                         {props.info.groupTitle}
//                         {
//                             (!props.info.status)?
//                             <Tag color='default' style={{marginLeft: '5px'}}>已取消</Tag> :
//                             (new Date(props.info.startTime) > new Date())?
//                             <Tag color='primary' style={{marginLeft: '5px'}}>未开始</Tag> :
//                             (new Date(props.info.endTime) > new Date())?
//                             <Tag color='success' style={{marginLeft: '5px'}}>进行中</Tag> :
//                             <Tag color='danger' style={{marginLeft: '5px'}}>已结束</Tag>
//                         }
//                     </div>
//                 }
//                 className='cardItem'
//                 extra={
//                     <RightOutline />
//                 }
//                 onHeaderClick={()=>{navigate('/view', { replace: false, state: { items: props.info.items, index: props.index, groupId: props.info.groupId, startTime: props.info.startTime, endTime: props.info.endTime } })}}
//                 style={{ 'borderRadius': '36px', alignItems: 'center', marginBottom: '5px', marginTop: '5px' }}
//             >
//                 <List>
//                     <List.Item prefix={text('起始时间')} extra={text(props.info.startTime)}></List.Item>
//                     <List.Item prefix={text('结束时间')} extra={text(props.info.endTime)}></List.Item>
//                     <List.Item prefix={text('物流方式')} extra={text(logisticsWays[props.info.logistics - 1])}></List.Item>
//                     <List.Item prefix={text('活动内容')} extra={
//                         (props.info.groupDescription === null || props.info.groupDescription.length < 12)? text(props.info.groupDescription) :
//                         <TextArea autoSize={{minRows: 0, maxRows: 20}} readOnly disabled value={props.info.groupDescription} style={{'--font-size': '14px'}} />
//                     }></List.Item>
//                 </List>

//                 <div className="footer2" onClick={e => e.stopPropagation()}>
//                     {
//                         (props.info.status)?
//                         <Space direction='horizontal'>
//                             <Button color='primary' size='mini' onClick={() => setQRVisible(true)}>
//                                 <SystemQRcodeOutline />
//                             </Button>
//                             <Button color='primary' size='mini' onClick={() => setModalVisible(true)}>修改信息</Button>
//                             { new Date(props.info.endTime) > new Date()  &&  <Button color='primary' size='mini' onClick={() => finishGroup()}>提前结束</Button> }
//                             <Button color='primary' size='mini' onClick={() => handleCancel()}>取消</Button>
//                         </Space> :
//                         <Space direction='horizontal'>
//                             <Button color='danger' size='mini' onClick={() => {}}>删除记录</Button>
//                         </Space>
//                     }
//                 </div>
//             </Card>

//             <Modal
//                 title="链接和二维码"
//                 visible={qrVisible}
//                 okText="确认"
//                 cancelText="取消"
//                 onCancel={() => { setQRVisible(false); }}
//                 onOk={() => { setQRVisible(false); }}
//                 footer={()=>{}}
//             >
//                 <ShowLinkAndQR groupId={props.info.groupId} />
//             </Modal>

//             <Modal
//                 title="修改团购信息"
//                 visible={modalVisible}
//                 okText="确认"
//                 cancelText="取消"
//                 onCancel={() => { setModalVisible(false); }}
//                 onOk={() => { setModalVisible(false); }}
//                 footer={()=>{}}
//             >

//                 <Form layout="horizontal" onFinish={(values)=>{ setModalVisible(false); modifyGroupBuy(values); }}
//                     footer={
//                         <Button block type='submit' color='primary' size='large'>
//                             修改
//                         </Button>
//                     }
//                     mode='card'
//                     form={groupBuyForm}
//                 >
//                     <Form.Header>修改团购信息</Form.Header>

//                     <Form.Item name='groupTitle' label='标题' initialValue={props.info.groupTitle} rules={[{ required: true, message: '标题不能为空' }]}>
//                         <Input placeholder='请输入团购活动标题' />
//                     </Form.Item>

//                     <Form.Item name='groupDescription' label='描述' initialValue={props.info.groupDescription}>
//                         <TextArea placeholder='请输入团购活动内容' autoSize={{minRows: 3, maxRows: 8}} />
//                     </Form.Item>

//                     <Form.Item name='startTime' label='开始时间' trigger='onConfirm' onClick={() => { setStartPickerVisible(true) }}
//                     rules={[{required: true, message: '请选择开始时间'}, { validator: (rule, value=null)=>{
//                         const endTime = groupBuyForm.getFieldValue('endTime');
//                         if(endTime && value > endTime)   return new Promise((resolve, reject) => reject('开始时间不能晚于结束时间'));
//                         return Promise.resolve();
//                     }}]} dependencies={['endTime']} initialValue={new Date(props.info.startTime)}>
//                         <DatePicker format={'YYYY-MM-DD HH:mm:ss'} visible={startPickerVisible} precision='second' onClose={() => { setStartPickerVisible(false) }} min={new Date()}>
//                             {value =>
//                             value ? ToYYYYMMDD(value) : '请选择时间'
//                             }
//                         </DatePicker>
//                     </Form.Item>

//                     <Form.Item name='endTime' label='截止时间' trigger='onConfirm' onClick={() => { setEndPickerVisible(true) }}
//                     rules={[{required: true, message: '请选择截止时间'}, { validator: (rule, value=null)=>{
//                     const startTime = groupBuyForm.getFieldValue('startTime');
//                     if(startTime && value < startTime)   return new Promise((resolve, reject) => reject('结束时间不能早于开始时间'));
//                     return Promise.resolve();
//                     }}]} dependencies={['startTime']} initialValue={new Date(props.info.endTime)}>
//                         <DatePicker visible={endPickerVisible} precision='second' onClose={() => { setEndPickerVisible(false) }} min={new Date()}>
//                             {value =>
//                             value ? ToYYYYMMDD(value) : '请选择时间'
//                             }
//                         </DatePicker>
//                     </Form.Item>

//                     <Form.Item name='logistics' label='物流方式' initialValue={[props.info.logistics]} trigger='onConfirm' rules={[{required: true, message: '请选择物流方式'}]}>
//                         <Selector
//                             options={logisticOptions}
//                             onChange={(v)=>{groupBuyForm.setFieldsValue({logistics: v})}}
//                         />
//                     </Form.Item>

//                     </Form>
//             </Modal>

//             <div style={{height: '10px', backgroundColor: 'lightblue'}}>
//             </div>
//         </div>
//     );
// }
