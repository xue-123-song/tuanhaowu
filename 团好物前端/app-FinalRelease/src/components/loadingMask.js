import React from 'react';
import { Mask, SpinLoading } from 'antd-mobile';

export default (props) => {
    return (
        <Mask visible={props.visible}>
            <div className="overlayContent">
                <SpinLoading style={{ '--size': '32px' }} />
            </div>
        </Mask>
    );
}