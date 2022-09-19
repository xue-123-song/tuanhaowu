import JSEncrypt from 'encryptlong';

const PUBLIC_KEY = 
`-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFuI/8ptJAqdqN7c5PXFvE4q0/
xckr+IKX96Xzvs8GvTWtFH7Iltl/G6qKS6CV+WIixwIr7TbXGj9EKELTufpdoUl6
M4jaowgtOrjSwwA6LLV5m/SMYkLUVYPqCladYUlgqN2eE2sUn8mx8BbMZJVxmwWF
7JCiFRWIElgRYEEc2wIDAQAB
-----END PUBLIC KEY-----`;

const PRIVATE_KEY = 
`-----BEGIN PRIVATE KEY-----
MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMW4j/ym0kCp2o3t
zk9cW8TirT/FySv4gpf3pfO+zwa9Na0UfsiW2X8bqopLoJX5YiLHAivtNtcaP0Qo
QtO5+l2hSXoziNqjCC06uNLDADostXmb9IxiQtRVg+oKVp1hSWCo3Z4TaxSfybHw
FsxklXGbBYXskKIVFYgSWBFgQRzbAgMBAAECgYEAhWWX/6WthOMUCV3AK9rsL1Gg
Ab63k2r+8Pishl7d3nYZmMSTJVJhO1i3HscXeRfgFgaeeHR643VZ6eof7Ck1uHQT
baukNV0GWIEHLNKY7ydx8v9wwKJ76yPcUdB9OarHcIsPuAWh2zmVJkZTLH1zt09d
jy6p9w6UUZh7Q+UV3IECQQD+UQ28agfyqZDrPQJKWOqqp3810VJusG5iyMk1UTdf
4eNQOSKaHKIMe4WVa/CWLplCeIhxgupxj90GgLSSoII5AkEAxwebHO2gnKm03Nko
X2xWaD/BgyhGTLUyZb3ruUg5E7GIcrfTTuoeHh5T1+cx3JLpjvI/+A82dFnNzad9
mCqHswJBAMobujHZhhV4Yp5AbviM73nOAU/Q2ZT0C9Xfwd/oenxttUwFDBWz2ajX
5ZKZJKsFv3DO/4hORWkYrsvDLz4z5ykCQCjtqnsvbpjo2TWVPpUo7Tfw7HVxQcui
c2Oyw5fNf1dKadnVbZHL2TeuWuWKOJm4aGkp7fewAS26DfVdnRk+nbsCQEi4jaHP
4n/MKJ6Ztx8erT2Kwc8++AE3Yz5jwO+OtcUzYwYO1nvb90rkdWxh9gYC4mlz1vTs
LJdm31IHQm34jyI=
-----END PRIVATE KEY-----`;

var encryptor = new JSEncrypt();
encryptor.setPublicKey(PUBLIC_KEY);
encryptor.setPrivateKey(PRIVATE_KEY);

const encrypt = (txt) => {
    return encryptor.encryptLong(txt);
}

const decrypt = (txt) => {
    return encryptor.decryptLong(txt);
}

export { encrypt, decrypt }