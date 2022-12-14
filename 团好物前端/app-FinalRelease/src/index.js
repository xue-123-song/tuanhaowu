import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'lib-flexible';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <App />
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

// import React, { useState, useEffect } from 'react';
// import { ChoroplethMap } from '@ant-design/maps';

// const DemoChoroplethMap = () => {
//   const [list, setData] = useState([]);

//   useEffect(() => {
//     asyncFetch();
//   }, []);

//   const asyncFetch = () => {
//     fetch('https://gw.alipayobjects.com/os/alisis/geo-data-v0.1.2/administrative-data/area-list.json')
//       .then((response) => response.json())
//       .then((json) => setData(json))
//       .catch((error) => {
//         console.log('fetch data failed', error);
//       });
//   };
//   const data = list
//     .filter(({ level }) => level === 'city')
//     .map((item) =>
//       Object.assign({}, item, {
//         value: Math.random() * 5000,
//       }),
//     );
//   const config = {
//     map: {
//       type: 'mapbox',
//       style: 'blank',
//       center: [120.19382669582967, 30.258134],
//       zoom: 3,
//       pitch: 0,
//     },
//     source: {
//       data: data,
//       joinBy: {
//         sourceField: 'adcode',
//         geoField: 'adcode',
//       },
//     },
//     viewLevel: {
//       level: 'country',
//       adcode: 100000,
//       granularity: 'city',
//     },
//     autoFit: true,
//     color: {
//       field: 'value',
//       value: ['#B8E1FF', '#7DAAFF', '#3D76DD', '#0047A5', '#001D70'],
//       scale: {
//         type: 'quantize',
//       },
//     },
//     style: {
//       opacity: 1,
//       stroke: '#ccc',
//       lineWidth: 0.6,
//       lineOpacity: 1,
//     },
//     label: {
//       visible: true,
//       field: 'name',
//       style: {
//         fill: '#000',
//         opacity: 0.8,
//         fontSize: 10,
//         stroke: '#fff',
//         strokeWidth: 1.5,
//         textAllowOverlap: false,
//         padding: [5, 5],
//       },
//     },
//     state: {
//       active: {
//         stroke: 'black',
//         lineWidth: 1,
//       },
//     },
//     tooltip: {
//       items: ['name', 'adcode', 'value'],
//     },
//     zoom: {
//       position: 'bottomright',
//     },
//     legend: {
//       position: 'bottomleft',
//     },
//   };

//   return <div style={{width: 500, height: 500}}>?????????hhh<ChoroplethMap {...config} /></div>;
// };

// const root = ReactDOM.createRoot(document.getElementById('root'));

// root.render(<DemoChoroplethMap />);

// reportWebVitals();