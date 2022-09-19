import React, { useState, useEffect } from 'react';
import { Card, Statistic, Row, Col, Radio } from 'antd';
import { Space } from 'antd-mobile';
import { doGet } from '../utils/ajax';
import { IntToPrice } from '../utils/transformPrice';
import { TableComponent, BarComponent, PieComponent } from './statistic';

const GroupStatistic = (props) => {
    return (
        <Space block direction='vertical'>
            <BasicDataCard groupId={props.groupId} />
            <SaleNumCard groupId={props.groupId} />
        </Space>
    );
}

const BasicDataCard = ({ groupId }) => {
    const [data, setData] = useState({
        totalSales: 0,
        withdraw: 0,
        participants: 0,
        cancelOrderNum: 0,
        ValidOrderNum: 0,
    });
    useEffect(() => {
        const toDo = async() => {
          let response = await doGet("/statistic/QueryData/" + groupId);
          console.log(response);
          setData(response.data);
        }
        toDo();
    }, [])

    return (
        <Card title='基本销售数据'>
            <Row gutter={[24, 24]} justify='center'>
                <Col span={8}><Statistic title='总订单数' value={data.ValidOrderNum + data.cancelOrderNum} /></Col>
                <Col span={8}><Statistic title='下单人数' value={data.participants} /></Col>
                <Col span={8}><Statistic title='浏览人数' value={data.visitTimes} /></Col>
                <Col span={12}><Statistic title={<span style={{ textAlign: 'center', width: '100%' }}>总销售额</span>} value={IntToPrice(data.totalSales)} prefix={<span style={{ fontSize: 12 }}>￥</span>} /></Col>
                <Col span={12}><Statistic title={<span style={{ textAlign: 'center', width: '100%' }}>退款金额</span>} value={IntToPrice(data.withdraw)}   prefix={<span style={{ fontSize: 12 }}>￥</span>} /></Col>
            </Row>
        </Card>
    );
}

const SaleNumCard = ({ groupId }) => {
    const [type, setType] = useState('条形图');
    const [data, setData] = useState([]);
    const columns = [
        {
          title: '排名',
          dataIndex: 'rank',
        },
        {
          title: '商品',
          dataIndex: 'name',
        },
        {
          title: '销量',
          dataIndex: 'saleNum',
        },
    ];

    useEffect(() => {
        const toDo = async() => {
            let arr = await doGet("/statistic/itemSellNum/" + groupId);
            arr.sort((a, b) => {
                return  b[1] - a[1];
            })
            let tmp = [];
            for(let i = 0; i < arr.length; ++i)
                tmp.push({ rank: i + 1, name: arr[i][0], saleNum: arr[i][1] });
            setData(tmp);
        }
        toDo();
    }, [])

    const pic = () => {
        switch(type) {
          case '表格': return <TableComponent columns={columns} data={data} />
          case '条形图': return <BarComponent data={data} xField='saleNum' yField='name' />
          case '扇形图': return <PieComponent data={data} colorField='name' angleField='saleNum' />
          default: return <></>
        }
    }

    return (
        <Card title={
            <>
                商品销量统计
                <span style={{ float: 'right' }}>
                    <Radio.Group size='small' buttonStyle='solid' defaultValue='条形图' onChange={e=>setType(e.target.value)}>
                      <Radio.Button value="表格">表格</Radio.Button>
                      <Radio.Button value="条形图">条形图</Radio.Button>
                      <Radio.Button value="扇形图">扇形图</Radio.Button>
                    </Radio.Group>
                </span>
            </>
        }>
            {
                pic()
            }
        </Card>
    );
}

export default GroupStatistic;
