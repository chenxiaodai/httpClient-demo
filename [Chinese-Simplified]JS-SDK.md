# 目录

- [概览](#概览)
- [版本说明](#版本说明)
  - [v0.2.0 更新说明](#v0.2.0-更新说明)
- [快速入门](#快速入门)
  - [安装或引入](#安装或引入)
  - [初始化代码](#初始化代码)
- [合约](#合约)
  - [合约示例](#合约示例)
  - [部署合约](#部署合约)
  - [合约call调用](#合约call调用)
  - [合约sendRawTransaction调用](#合约sendrawtransaction调用)
- [web3](#web3)
  - [web3 eth相关 (标准JSON RPC )](#web3-eth相关-标准json-rpc)
    - [getBalance](#getbalance)
    - [getStorageAt](#getstorageat)
    - [getCode](#getcode)
    - [getBlock](#getblock)
    - [getBlockByNumber](#getblockbynumber)
    - [getUncle](#getuncle)
    - [getBlockTransactionCountByHash](#getblocktransactioncountbyhash)
    - [getBlockTransactionCountByNumber](#getblocktransactioncountbynumber)
    - [getTransactionByHash](#gettransactionbyhash)
    - [getTransactionFromBlock](#gettransactionfromblock)
    - [getTransactionReceipt](#gettransactionreceipt)
    - [getTransactionCount](#gettransactioncount)
    - [estimateGas](#estimategas)
    - [mining](#mining)
    - [hashrate](#hashrate)
    - [syncing](#syncing)
    - [gasPrice](#gasprice)
    - [accounts](#accounts)
    - [blockNumber](#blocknumber)
    - [protocolVersion](#protocolversion)
    - [call](#call)
    - [sendTransaction](#sendTransaction)
    - [sendRawTransaction](#sendrawtransaction)
    - [contract](#contract)
      - [getPlatONData](#getPlatONData)
      - [decodePlatONCall](#decodePlatONCall)
      - [decodePlatONLog](#decodePlatONLog)
  - [web3.version](#web3.version)
    - [api](#api)
    - [node](#node)
    - [network](#network)
    - [ethereum](#ethereum)
    - [whisper](#whisper)
  - [web3.db](#web3.db)

## 概览
> Javascript SDK是PlatON面向js开发者，提供的PlatON公链的js开发工具包

## 版本说明

### v0.2.0 更新说明

1. 支持PlatON的Sophia智能合约

### 快速入门

### 安装或引入

通过node.js引入：

`cnpm i https://github.com/PlatONnetwork/client-sdk-js`

### 初始化代码

然后你需要创建一个web3的实例，设置一个provider。为了保证你不会覆盖一个已有的provider，比如使用Mist时有内置，需要先检查是否web3实例已存在。

```js
if (typeof web3 !== 'undefined') {
    web3 = new Web3(web3.currentProvider);
} else {
    web3 = new Web3(new Web3.providers.HttpProvider('http://localhost:6789'));
}
````

### 合约

Sophia智能合约的编写及其ABI(wasm文件)和BIN(json文件)生成方法请参考 [wiki](https://github.com/PlatONnetwork/wiki/wiki)

#### 合约示例

[^_^]:
    namespace platon {
        class ACC : public token::Token {
        public:
            ACC(){}
            void init(){
                Address user(std::string("0xa0b21d5bcc6af4dda0579174941160b9eecb6916"), true);
                initSupply(user, 199999);
            }

            void create(const char *addr, uint64_t asset){
                Address user(std::string(addr), true);
                Token::create(user, asset);
            }

            uint64_t getAsset(const char *addr) const {
                Address user(std::string(addr), true);
                return Token::getAsset(user);
            }

            void transfer(const char *addr, uint64_t asset) {
                Address user(std::string(addr), true);
                Token::transfer(user, asset);
            }
        };
    }

    PLATON_ABI(platon::ACC, create)
    PLATON_ABI(platon::ACC, getAsset)
    PLATON_ABI(platon::ACC, transfer)
    //platon autogen begin
    extern "C" {
        void create(const char * addr,unsigned long long asset) {
            platon::ACC ACC_platon;
            ACC_platon.create(addr,asset);
        }
        unsigned long long getAsset(const char * addr) {
            platon::ACC ACC_platon;
            return ACC_platon.getAsset(addr);
        }
        void transfer(const char * addr,unsigned long long asset) {
            platon::ACC ACC_platon;
            ACC_platon.transfer(addr,asset);
        }
        void init() {
            platon::ACC ACC_platon;
            ACC_platon.init();
        }
    }
    //platon autogen end

#### 部署合约

##### 接口声明

    MyContract.deploy(deployData [, callback])

##### 参数说明

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|deployData |String |必选| 16进制格式的签名交易数据|
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

##### 示例

````js
const Web3 = require('web3'),
    fs = require('fs'),
    path = require('path'),
    Tx = require('ethereumjs-tx'),
    abi = require('./multisig.cpp.abi.json') //应用程序二进制接口
    ;

const provider = 'http://localhost:6789',
    privateKey='fd098d39e56115762cf28d7d223a4303a42a42451da6e7da37cb40a81a246d98',//私钥
    address='0xf216d6e4c17097a60ee2b8e5c88941cd9f07263b'

const web3 = new Web3(new Web3.providers.HttpProvider(provider))

const MyContract  = web3.eth.contract(abi)

const source = fs.readFileSync(path.join(__dirname, './demo.wasm'));//WebAssembly的二进制代码 bin

const platONData = MyContract.deploy.getPlatONData(source)

const params = {
    nonce: web3.eth.getTransactionCount(address),
    gas: "0x506709",
    gasPrice: "0x8250de00",
    data: platONData,
    from:address
}

const daployData=sign(privateKey,params)

let myContractReturned = MyContract.deploy(daployData, (err, myContract)=> {
    if (!err) {
        if (!myContract.address) {
            console.log("contract deploy transaction hash: " + myContract.transactionHash) // 部署合约的交易哈希值
        } else {
            // 合约发布成功
            console.log("contract deploy transaction address: " + myContract.address)// 部署合约的地址
            const receipt = web3.eth.getTransactionReceipt(myContract.transactionHash);
            console.log(`contract deploy receipt:`, receipt);
        }
    } else {
        console.log(`contract deploy error:`, err)
    }
});

function sign(privateKey, data) {
    const key = new Buffer(privateKey, 'hex')
    const tx = new Tx(data)
    tx.sign(key)

    const serializeTx = tx.serialize()
    const result = '0x' + serializeTx.toString('hex')
    return result
}
````

#### 合约call调用

> 调用合约函数，返回常量值

##### 接口声明

    web3.eth.call(Object [, defaultBlock] [, callback])

##### 参数说明

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|Object |String |必选| 返回一个交易对象，同`web3.eth.sendTransaction`。与`sendTransaction`的区别在于，`from`属性是可选的|
|defaultBlock|Number/String |可选|如果不设置此值使用`web3.eth.defaultBlock`设定的块，否则使用指定的块|
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

##### 示例

````js
const data = contract.getOwners.getPlatONData()

const result = web3.eth.call({
    from: web3.eth.accounts[0],
    to: contract.address,
    data: data
});

console.log('call result:', result);

contract.decodePlatONCall(result)
````

#### 合约sendRawTransaction调用

> 发送通过私钥签名的交易

##### 接口声明

    web3.eth.sendRawTransaction(signedTransactionData [, callback])

##### 参数说明

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|signedTransactionData |String |必选|16进制格式的签名交易数据|
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

##### 示例

````js
const Tx = require('ethereumjs-tx');

const privateKey='4484092b68df58d639f11d59738983e2b8b81824f3c0c759edd6773f9adadfe7',
    param_from = web3.eth.accounts[0],
    param_to = wallet.address,
    param_assert = 4
    ;

const data = myContractInstance.transfer02.getPlatONData(param_from, param_to, param_assert);

const hash = web3.eth.sendRawTransaction(sign(privateKey,getParams(data)))
console.log('hash', hash);//0xb1335d4db521ddc0b390448f919e5b5af1258b29e7ab4e0d68b0ef315af0cf5f

const data=web3.eth.getTransactionReceipt(hash);

let res = myContractInstance.decodePlatONLog(data.logs[0]);

function sign(privateKey, data) {
    const key = new Buffer(privateKey, 'hex')
    const tx = new Tx(data)
    tx.sign(key)

    const serializeTx = tx.serialize()
    const result = '0x' + serializeTx.toString('hex')
    return result
}

function getParams(data = '', value = "0x0") {
    const nonce = web3.eth.getTransactionCount(wallet.address)
    value = web3.toHex(web3.toWei(value, 'ether'));

    const params = {
        from: '0xf216d6e4c17097a60ee2b8e5c88941cd9f07263b',
        gasPrice: 22 * 10e9,
        gas: 80000,
        to: myContractInstance.address,
        value,
        data,
        nonce
    }

    return params
}
````

### web3

----

### web3 eth相关 (标准JSON RPC )

#### getBalance

> 获得在指定区块时给定地址的余额

##### 接口声明

    web3.eth.getBalance(addressHexString [, defaultBlock] [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|addressHexString |String |必选|要查询余额的地址|
|defaultBlock|Number/String |可选|如果不设置此值使用`web3.eth.defaultBlock`设定的块，否则使用指定的块|
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`String` - 一个包含给定地址的当前余额的`BigNumber`实例，单位为`wei`。

##### 示例

````js
    const balance = web3.eth.getBalance("0xf216d6e4c17097a60ee2b8e5c88941cd9f07263b");
    console.log(balance); // instanceof BigNumber
    console.log(balance.toString(10)); // '1000000000000'
    console.log(balance.toNumber()); // 1000000000000
````

#### getStorageAt

> 获取某一地址上指定位置的存储数据

##### 接口声明

    web3.eth.getStorageAt(addressHexString, position [, defaultBlock] [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|addressHexString |String |必选|要获得存储的地址|
|position  |Number|必选|要查询余额的地址|
|defaultBlock|Number/String |可选|如果不设置此值使用`web3.eth.defaultBlock`设定的块，否则使用指定的块|
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`String` - 给定位置的存储值

##### 示例

````js
const state = web3.eth.getStorageAt("0x407d73d8a49eeb85d32cf465507dd71d507100c1", 0);
console.log(state); // "0x03"
````

#### getCode

> 获取某一地址上指定位置的数据

##### 接口声明

    web3.eth.getCode(addressHexString [, defaultBlock] [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|addressHexString |String |必选|要查询余额的地址|
|defaultBlock|Number/String |可选|如果不设置此值使用`web3.eth.defaultBlock`设定的块，否则使用指定的块|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`String` - 给定地址合约编译后的字节代码

##### 示例

````js
    const code = web3.eth.getCode("0xd5677cf67b5aa051bb40496e68ad359eb97cfbf8");
    console.log(code);
````

#### getBlock

> 获取某一地址上指定位置的区块

##### 接口声明

    web3.eth.getBlock(blockHashOrBlockNumber [, returnTransactionObjects] [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|blockHashOrBlockNumber  |Number/String  |可选|如果未传递参数，默认使用`web3.eth.defaultBlock`定义的块，否则使用指定区块|
|returnTransactionObjects |Boolean |可选|默认值为`false`。`true`会将区块包含的所有交易作为对象返回。否则只返回交易的哈希|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

TODO

##### 示例

````js
    constinfo = web3.eth.getBlock('0xb1335d4db521ddc0b390448f919e5b5af1258b29e7ab4e0d68b0ef315af0cf5f');
    console.log(info);
````

#### getUncle

> 通过区块哈希和索引获取uncle区块

##### 接口声明

    web3.eth.getUncle(blockHashStringOrNumber, uncleNumber [, returnTransactionObjects] [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|blockHashStringOrNumber  |Number/String  |必选|如果未传递参数，默认使用`web3.eth.defaultBlock`定义的块，否则使用指定区块|
|uncleNumber |Number  |必选|`uncle`的序号|
|returnTransactionObjects |Boolean |可选|默认值为`false`。`true`会将区块包含的所有交易作为对象返回。否则只返回交易的哈希|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Object` - 返回的叔块。返回值参考[getBlock](#getblock)。

##### 示例

````js
    constuncle = web3.eth.getUncle(500, 0);
    console.log(uncle);
````

#### getBlockTransactionCount

> 根据hash获取对应区块的交易数量

##### 接口声明

    web3.eth.getBlockTransactionCount(hashStringOrBlockNumber [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|blockHashStringOrNumber  |Number/String  |必选|交易哈希/块高。如果未传递参数，默认使用`web3.eth.defaultBlock`定义的块，否则使用指定区块|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Nubmer` - 给定区块的交易数量。

##### 示例

````JS
constnumber = web3.eth.getBlockTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
console.log(number); // 1
````

#### getTransaction

> 根据哈希获取交易对象

##### 接口声明

    web3.eth.getTransaction(transactionHash [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|transactionHash   |String  |必选|交易哈希|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Object` - 一个交易对象
- `hash`: `String` - 32字节，交易的哈希值。
- `nonce`: `Number` - 交易的发起者在之前进行过的交易数量。
- `blockHash`: `String` - 32字节。交易所在区块的哈希值。当这个区块处于pending将会返回null。
- `blockNumber`: `Number` - 交易所在区块的块号。当这个区块处于pending将会返回null。
- `transactionIndex`: `Number` - 整数。交易在区块中的序号。当这个区块处于pending将会返回null。
- `from`: `String` - 20字节，交易发起者的地址。
- `to`: `String` - 20字节，交易接收者的地址。当这个区块处于pending将会返回null。
- `value`: `BigNumber` - 交易附带的货币量，单位为Wei。
- `gasPrice`: `BigNumber` - 交易发起者配置的gas价格，单位是wei。
- `gas`: `Number` - 交易发起者提供的gas。.
- `input`: `String` - 交易附带的数据。


##### 示例

    TODO

#### getTransactionFromBlock

> 根据区块哈希获取指定下标区块上的交易对象

##### 接口声明

    web3.eth.getTransactionFromBlock(blockHash, transactionIndex [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|blockHash  |Number/String  |必选|区块号或哈希。或者是`earliest`，`latest`或`pending`。查看`web3.eth.defaultBlock`了解可选值。|
|transactionIndex |Number  |必选|交易的序号|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Object` - 交易对象，详见`web3.eth.getTransaction`

##### 示例

````JS
    consttransaction = web3.eth.getTransactionFromBlock('0x4534534534', 2);
    console.log(transaction); //详见`web3.eth.getTransaction`
````

#### getTransactionReceipt

> 根据交易哈希获取交易回执

##### 接口声明

    web3.eth.getTransactionReceipt(transactionHash [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|transactionHash   |String  |必选|交易哈希|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Object` - 交易回执，如果找不到返回null

- `blockHash`: `String` - 32字节，这个交易所在区块的哈希。
- `blockNumber`: `Number` - 交易所在区块的块号。
- `transactionHash`: `String` - 32字节，交易的哈希值。
- `transactionIndex`: `Number` - 交易在区块里面的序号，整数。
- `from`: `String` - 20字节，交易发送者的地址。
- `to`: `String` - 20字节，交易接收者的地址。如果是一个合约创建的交易，返回null。
- `cumulativeGasUsed`: `Number` - 当前交易执行后累计花费的gas总值10。
- `gasUsed`: `Number` - 执行当前这个交易单独花费的gas。
- `contractAddress`: `String` - 20字节，创建的合约地址。如果是一个合约创建交易，返回合约地址，其它情况返回`null`。
- `logs`: `Array` - 这个交易产生的日志对象数组。

##### 示例

````js
constreceipt = web3.eth.getTransactionReceipt('0xb1335d4db521ddc0b390448f919e5b5af1258b29e7ab4e0d68b0ef315af0cf5f');
console.log(receipt);

````

#### getTransactionCount

> 获取nonce（某一地址上的交易数量）

##### 接口声明

    web3.eth.getTransactionCount(addressHexString [, defaultBlock] [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|addressHexString  |String  |必选| 要获得交易数的账户地址|
|defaultBlock |Number/String  |可选|如果未传递参数，默认使用`web3.eth.defaultBlock`定义的块，否则使用指定区块|
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Number` - 指定地址发送的交易数量

##### 示例

````js
constnumber = web3.eth.getTransactionCount("0xf216d6e4c17097a60ee2b8e5c88941cd9f07263b");
console.log(number); // 6
````

#### estimateGas

> 获取对应交易预估手续费

##### 接口声明

    web3.eth.estimateGas(callObject [, callback])

##### 参数

同web3.eth.sendTransaction，所有的属性都是可选的。

**返回值 或 回调**

`Number` - 对应call/transcation交易预估手续费

##### 示例

````js
var result = web3.eth.estimateGas({
    to: "0xc4abd0339eb8d57087278718986382264244252f",
    data: "0xc6888fa10000000000000000000000000000000000000000000000000000000000000003"
});
console.log(result); // "0x0000000000000000000000000000000000000000000000000000000000000015"
````

#### mining

> 获取节点是否在挖矿

##### 接口声明

    同步方式：

        web3.eth.mining

    异步方式：

        web3.eth.getMining(callback(error, result){ ... })

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Boolean` - true 表示配置挖矿，否则表示没有。

##### 示例

````js
var mining = web3.eth.mining;
console.log(mining); // true or false
````

#### hashrate

> 获取节点挖矿每秒中hash的数量

##### 接口声明

    同步方式：

        web3.eth.hashrate

    异步方式：

        web3.eth.getHashrate(callback(error, result){ ... })

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Number` - 每秒的哈希数

##### 示例

````js
var hashrate = web3.eth.hashrate;
console.log(hashrate);
````

#### syncing

> 获取节点的同步状态

##### 接口声明

    同步方式：

        web3.eth.syncing

    异步方式：

        web3.eth.getSyncing(callback(error, result){ ... })

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Object`|`Boolean` - 如果正在同步，返回含下面属性的同步对象。否则返回false。
`startingBlock`：`Number` - 同步开始区块号
`currentBlock`: `Number` - 节点当前正在同步的区块号
`highestBlock`: `Number` - 预估要同步到的区块

##### 示例

````js
var sync = web3.eth.syncing;
console.log(sync);
````

#### gasPrice

> 获取最新快的交易单价

##### 接口声明

    同步方式：

        web3.eth.gasPrice

    异步方式：

        web3.eth.getGasPrice(callback(error, result){ ... })

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`BigNumber` - 当前的`gas`价格的`BigNumber`实例，以`wei`为单位

##### 示例

````js
var gasPrice = web3.eth.gasPrice;
console.log(gasPrice.toString(10)); // "10000000000000"
````

#### accounts

> 获取节点对应的账户

##### 接口声明

    同步方式：

        web3.eth.accounts

    异步方式：

        web3.eth.getAccounts(callback(error, result){ ... })

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callback|Funciton|可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`Array` - 节点持有的帐户列表。

##### 示例

````js
var accounts = web3.eth.accounts;
console.log(accounts);
````

#### blockNumber

> 获取节点当前交易区块数量

##### 接口声明

    同步方式：

        web3.eth.blockNumber

    异步方式：

        web3.eth.getBlockNumber(callback(error, result){ ... })

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

**返回值 或 回调**

`number`  当前区块号

##### 示例

````js
var number = web3.eth.blockNumber;
console.log(number); // 2744
````

#### call

同：[合约call调用](#合约call调用)

#### sendTransaction

> 发送交易

##### 接口声明

    web3.eth.sendTransaction(transactionObject [, callback])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|transactionObject|Object   |可选|要发送的交易对象|
|callback|Funciton  |可选|回调函数，用于支持异步的方式执行|

###### transactionObject参数：

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|from|String|必选|指定的发送者的地址。如果不指定，使用`web3.eth.defaultAccount`|
|to|String|可选|交易消息的目标地址，如果是合约创建，则不填|
|value|String/Number/BigNumber|可选|交易携带的货币量，以`wei`为单位。如果合约创建交易，则为初始的金额|
|gas|String/Number/BigNumber|可选|默认是自动，交易可使用的`gas`，未使用的`gas`会退回|
|gasPrice|String/Number/BigNumber|可选|默认是自动确定，交易的`gas`价格，默认是网络`gas`价格的平均值|
|data|String|可选|或者包含相关数据的字节字符串，如果是合约创建，则是初始化要用到的代码|
|nonce|Number |可选|整数，使用此值，可以允许你覆盖你自己的相同`nonce`的，正在`pending`中的交易|

**返回值 或 回调**

`String` - 32字节的交易哈希串。用16进制表示。

##### 示例

````js

````


#### sendRawTransaction

同：[合约sendrawtransaction调用](#合约sendrawtransaction调用)

#### contract

##### 接口声明

    web3.eth.contract(abi)

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|abi|Array|必选|应用程序二进制接口对象|

**返回值 或 回调**

`Object` - 一个合约对象

##### 示例

````js
const abi=[
    {
        "name": "initWallet",
        "inputs": [
            {
                "name": "owner",
                "type": "string"
            },{
                "name": "required",
                "type": "uint64"
            }
        ],
        "outputs": [],
        "constant": "false",
        "type": "function"
    },{
        "name": "submitTransaction",
        "inputs": [
            {
                "name": "destination",
                "type": "string"
            },{
                "name": "from",
                "type": "string"
            },{
                "name": "value",
                "type": "uint64"
            },{
                "name": "data",
                "type": "string"
            },{
                "name": "len",
                "type": "uint64"
            },{
                "name": "time",
                "type": "uint64"
            },{
                "name": "fee",
                "type": "uint64"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "uint64"
            }
        ],
        "constant": "false",
        "type": "function"
    },{
        "name": "confirmTransaction",
        "inputs": [
            {
                "name": "transactionId",
                "type": "uint64"
            }
        ],
        "outputs": [],
        "constant": "false",
        "type": "function"
    },{
        "name": "revokeConfirmation",
        "inputs": [
            {
                "name": "transactionId",
                "type": "uint64"
            }
        ],
        "outputs": [],
        "constant": "false",
        "type": "function"
    },{
        "name": "executeTransaction",
        "inputs": [
            {
                "name": "transactionId",
                "type": "uint64"
            }
        ],
        "outputs": [],
        "constant": "false",
        "type": "function"
    },{
        "name": "isConfirmed",
        "inputs": [
            {
                "name": "transactionId",
                "type": "uint64"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "int32"
            }
        ],
        "constant": "true",
        "type": "function"
    },{
        "name": "getRequired",
        "inputs": [],
        "outputs": [
            {
                "name": "",
                "type": "uint64"
            }
        ],
        "constant": "true",
        "type": "function"
    },{
        "name": "getConfirmationCount",
        "inputs": [
            {
                "name": "transactionId",
                "type": "uint64"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "uint64"
            }
        ],
        "constant": "true",
        "type": "function"
    },{
        "name": "getTransactionCount",
        "inputs": [
            {
                "name": "pending",
                "type": "int32"
            },{
                "name": "executed",
                "type": "int32"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "uint64"
            }
        ],
        "constant": "true",
        "type": "function"
    },{
        "name": "getTransactionList",
        "inputs": [
            {
                "name": "from",
                "type": "uint64"
            },{
                "name": "to",
                "type": "uint64"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "constant": "true",
        "type": "function"
    },{
        "name": "getOwners",
        "inputs": [],
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "constant": "true",
        "type": "function"
    },{
        "name": "getConfirmations",
        "inputs": [
            {
                "name": "transactionId",
                "type": "uint64"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "constant": "true",
        "type": "function"
    },
    {
        "name": "getTransactionIds",
        "inputs": [
            {
                "name": "from",
                "type": "uint64"
            },{
                "name": "to",
                "type": "uint64"
            },{
                "name": "pending",
                "type": "int32"
            },{
                "name": "executed",
                "type": "int32"
            }
        ],
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "constant": "true",
        "type": "function"
    }
]

const MyContract = web3.eth.contract(abi);

const myContractInstance = MyContract.at('0x91b0ac240b62de2f0152cac322c6c5eafe730a84');

````

````js
var MyContract = web3.eth.contract(abi);

// instantiate by address
var contractInstance = MyContract.at([address]);

// deploy new contract
var contractInstance = MyContract.new([contructorParam1] [, contructorParam2], {data: '0x12345...', from: myAccount, gas: 1000000});

// Get the data to deploy the contract manually
var contractData = MyContract.new.getData([contructorParam1] [, contructorParam2], {data: '0x12345...'});
// contractData = '0x12345643213456000000000023434234'
````

#### contract.getPlatONData

##### 接口声明

    MyContract.myMethod.getPlatONData(param1 [, param2, ...])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|param1/param2/...|String/Number|必选|零或多个函数参数|

**返回值 或 回调**

`Object` - 一个合约对象

##### 示例

#### contract.decodePlatONCall

> 根据abi中fnName下的outputs类型解析call返回结果

##### 接口声明

    MyContract.decodePlatONCall( callResult, [fnName])

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|callResult|String|必选|应用程序二进制接口对象|
|fnName|String|可选|不传，callResult将string解析callResult,如果传了，会找到abi中name等于fnName下的outputs，按照outputs.type解析|

**返回值 或 回调**

`code` - 0表示成功
`data` - 解析出来的数据

##### 示例

````js
    MyContract.decodePlatONCall( '0x',)
````

#### contract.decodePlatONLog

> 解析交易回执中logs的某一项

##### 接口声明

    MyContract.decodePlatONLog(log)

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|log||Object|交易回执中logs的某一项|

**返回值 或 回调**

`Array` - 解析后的日志数组

##### 示例

````js
const data=web3.eth.getTransactionReceipt('0xb1335d4db521ddc0b390448f919e5b5af1258b29e7ab4e0d68b0ef315af0cf5f');

let res = myContractInstance.decodePlatONLog(data.logs[0]);
````

### web3.version

#### api

> 获取以太坊js的api版本

##### 接口声明

    web3.version.api

##### 参数

无

**返回值 或 回调**

`String` - web3.js的api版本

##### 示例

````js
var version = web3.version.api;
console.log(version);//"0.20.6"
````

#### node

> 获取客户端或节点的版本信息

##### 接口声明

    同步方式：

        web3.verison.node

    异步方式：

        web3.version.getNode(callback(error, result){ ... })

##### 参数

无

**返回值 或 回调**

`String` - 客户端或节点的版本信息

##### 示例

````js
var version = web3.version.node;
console.log(version);//"Geth/platon/v1.8.16-stable-375108ee/linux-amd64/go1.11"
````

#### network

> 获取网络协议版本

##### 接口声明

    同步方式：

        web3.version.network

    异步方式：

        web3.version.getNetwork(callback(error, result){ ... })

##### 参数

无

**返回值 或 回调**

`String` - 网络协议版本

##### 示例

````JS
var version = web3.version.network;
console.log(version);//"1"
````

#### ethereum

> 获取网络协议版本

##### 接口声明

    同步方式：

        web3.version.ethereum

    异步方式：

        web3.version.getEthereum(callback(error, result){ ... })

##### 参数

无

**返回值 或 回调**

`String` - 以太坊的协议版本

##### 示例

````JS
var version = web3.version.ethereum;
console.log(version);//"0x3f"
````

#### whisper

> 获取协议的版本

##### 接口声明

    同步方式：

        web3.version.whisper

    异步方式：

        web3.version.getWhisper(callback(error, result){ ... })

##### 参数

无

**返回值 或 回调**

`String` - 协议的版本

##### 示例

````JS
var version = web3.version.whisper;
console.log(version);
````

### web3.db

#### web3.db.putString

> web3.db.putString(db, key, value)
> 这个方法应当在我们打算以一个本地数据库的级别存储一个字符串时使用。

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|db|String |必选|存储使用的数据库|
|key|String |必选|存储的键|
|value|String |必选|存储的值|

**返回值**

`Boolean` - `true`表示成功，否则返回`false`。

##### 示例

    web3.db.putString('testDB', 'key', 'myString') // true


####

>

##### 参数

| 名称 |类型|属性|含义|
| :------: |:------: |:------: | :------: |
|address|EthereumAddress|必选|账户地址|
|position|Int|必选|存储索引地址|
|block|EthereumQuantityTag|必选|区块高度类型|
|response|Web3ResponseCompletion|必选|回调|

**返回值**
无

##### 示例
