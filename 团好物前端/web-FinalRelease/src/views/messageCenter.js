import React from 'react';
import Header from '../components/header';
import { List, Image  } from 'antd-mobile';

const MessageCenter = () => {
    const users = [
        {
          id: '1',
          avatar:
            'https://images.unsplash.com/photo-1548532928-b34e3be62fc6?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
          name: 'Novalee Spicer',
          description: 'Deserunt dolor ea eaque eos',
          extra: '2022年12月12日'
        },
        {
          id: '2',
          avatar:
            'https://images.unsplash.com/photo-1493666438817-866a91353ca9?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=b616b2c5b373a80ffc9636ba24f7a4a9',
          name: 'Sara Koivisto',
          description: 'Animi eius expedita, explicabo',
          extra: '2022年12月12日'
        },
        {
          id: '3',
          avatar:
            'https://images.unsplash.com/photo-1542624937-8d1e9f53c1b9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
          name: 'Marco Gregg',
          description: 'Ab animi cumque eveniet ex harum nam odio omnis',
          extra: '2022年12月12日'
        },
        {
          id: '4',
          avatar:
            'https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
          name: 'Edith Koenig',
          description: 'Commodi earum exercitationem id numquam vitae',
          extra: '2022年12月12日'
        },
        {
            id: '5',
            avatar:
              'https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
            name: 'Edith Koenig',
            description: 'Commodi earum exercitationem id numquam vitae',
            extra: '2022年12月12日'
          },
          {
            id: '6',
            avatar:
              'https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
            name: 'Edith Koenig',
            description: 'Commodi earum exercitationem id numquam vitae',
            extra: '2022年12月12日'
          },
          {
            id: '7',
            avatar:
              'https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
            name: 'Edith Koenig',
            description: 'Commodi earum exercitationem id numquam vitae',
            extra: '2022年12月12日'
          },
          {
            id: '8',
            avatar:
              'https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
            name: 'Edith Koenig',
            description: 'Commodi earum exercitationem id numquam vitae',
            extra: '2022年12月12日'
          },
          {
            id: '9',
            avatar:
              'https://images.unsplash.com/photo-1546967191-fdfb13ed6b1e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ',
            name: 'Edith Koenig',
            description: 'Commodi earum exercitationem id numquam vitae',
            extra: '2022年12月12日'
          },
    ]
    return (
        <div className='app_container'>
            <Header title='消息中心' onBack={() => window.history.back()} />
            <List>
                {users.map(user => (
                    <List.Item
                    key={user.name}
                    prefix={
                        <Image
                        src={user.avatar}
                        fit='cover'
                        width={40}
                        height={40}
                        />
                    }
                    description={user.description}
                    >
                        <div style={{ position: 'relative' }}>
                            <span>{user.name}</span>
                            <span style={{ position: 'absolute', right: '0px', fontSize: '12px', top: '25%', color: '#999999' }}>{user.extra}</span>
                        </div>
                    </List.Item>
                ))}
                </List>
        </div>
    );
}

export default MessageCenter;