import React, { useEffect, useState } from 'react';
import { Table } from 'antd';
import { Bar } from '@ant-design/plots';
import { Pie } from '@ant-design/plots';
import { ChoroplethMap } from '@ant-design/charts';

export const TableComponent = ({columns, data}) => (
    <div>
        <Table columns={columns} dataSource={data} size="middle" bordered pagination={false} />
    </div>
);

export const PieComponent = ({data, colorField, angleField}) => {
    const config = {
        appendPadding: 10,
        data,
        angleField: angleField,
        colorField: colorField,
        radius: 0.9,
        label: {
          type: 'inner',
          offset: '-30%',
          content: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
          style: {
            fontSize: 14,
            textAlign: 'center',
          },
        },
        interactions: [
          {
            type: 'element-active',
          },
        ],
    };
    return <Pie {...config} />;
};

export const BarComponent = ({ data, xField, yField }) => {
    const config = {
      data,
      xField: xField,
      yField: yField,
      seriesField: yField,
      legend: {
        position: 'top-left',
      },
    };
    return <Bar {...config} />;
};


export const MapComponent = () => {
  const [list, setData] = useState([]);

  useEffect(() => {
    asyncFetch();
  }, []);

  const asyncFetch = () => {
    fetch('https://gw.alipayobjects.com/os/alisis/geo-data-v0.1.2/administrative-data/area-list.json')
      .then((response) => response.json())
      .then((json) => setData(json))
      .catch((error) => {
        console.log('fetch data failed', error);
      });
  };
  const data = list
    .filter(({ level }) => level === 'city')
    .map((item) =>
      Object.assign({}, item, {
        value: Math.random() * 5000,
      }),
    );
  const config = {
    map: {
      type: 'mapbox',
      style: 'blank',
      center: [120.19382669582967, 30.258134],
      zoom: 3,
      pitch: 0,
    },
    source: {
      data: data,
      joinBy: {
        sourceField: 'adcode',
        geoField: 'adcode',
      },
    },
    viewLevel: {
      level: 'country',
      adcode: 100000,
      granularity: 'city',
    },
    autoFit: true,
    color: {
      field: 'value',
      value: ['#B8E1FF', '#7DAAFF', '#3D76DD', '#0047A5', '#001D70'],
      scale: {
        type: 'quantize',
      },
    },
    style: {
      opacity: 1,
      stroke: '#ccc',
      lineWidth: 0.6,
      lineOpacity: 1,
    },
    label: {
      visible: true,
      field: 'name',
      style: {
        fill: '#000',
        opacity: 0.8,
        fontSize: 10,
        stroke: '#fff',
        strokeWidth: 1.5,
        textAllowOverlap: false,
        padding: [5, 5],
      },
    },
    state: {
      active: {
        stroke: 'black',
        lineWidth: 1,
      },
    },
    tooltip: {
      items: ['name', 'adcode', 'value'],
    },
    zoom: {
      position: 'bottomright',
    },
    legend: {
      position: 'bottomleft',
    },
  };
  console.log(data);
  return <div style={{width: '100%', height: 500}} id='con'><ChoroplethMap {...config} /></div>;
};
