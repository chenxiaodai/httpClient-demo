-   [概览](#概览)
-   [版本说明](#版本说明)
    -   [v0.2.0 更新说明](#v0.2.0-更新说明)
-   [快速入门](#快速入门)
    -   [安装或引入](#安装或引入)
    -   [初始化代码](#初始化代码)
-   [合约](#合约)
    -   [合约示例](#合约示例)
    -   [合约骨架生成](#合约骨架生成)
    -   [加载合约](#加载合约)
    -   [部署合约](#部署合约)
    -   [合约call调用](#合约call调用)
    -   [合约sendRawTransaction调用](#合约sendrawtransaction调用)
    -   [合约event](#合约event)
-   [web3](#web3)
    -   [web3 eth相关 (标准JSON RPC )](#web3-eth相关-标准json-rpc)
        -   [ethAccounts](#ethaccounts)
        -   [ethBlockNumber](#ethblocknumber)
        -   [ethCall](#ethcall)
        -   [ethCoinbase](#ethcoinbase)
        -   [ethEstimateGas](#ethestimategas)
        -   [ethGasPrice](#ethgasprice)
        -   [ethGetBalance](#ethgetbalance)
        -   [ethGetBlockByHash](#ethgetblockbyhash)
        -   [ethGetBlockByNumber](#ethgetblockbynumber)
        -   [ethGetBlockTransactionCountByHash](#ethgetblocktransactioncountbyhash)
        -   [ethGetBlockTransactionCountByNumber](#ethgetblocktransactioncountbynumber)
        -   [ethGetCode](#ethgetcode)
        -   [ethGetFilterChanges](#ethgetfilterchanges)
        -   [ethGetFilterLogs](#ethgetfilterlogs)
        -   [ethGetLogs](#ethgetlogs)
        -   [ethGetStorageAt](#ethgetstorageat)
        -   [ethGetTransactionByBlockHashAndIndex](#ethgettransactionbyblockhashandindex)
        -   [ethGetTransactionByBlockNumberAndIndex](#ethgettransactionbyblocknumberandindex)
        -   [ethGetTransactionByHash](#ethgettransactionbyhash)
        -   [ethGetTransactionCount](#ethgettransactioncount)
        -   [ethGetTransactionReceipt](#ethgettransactionreceipt)
        -   [ethGetWork](#ethgetwork)
        -   [ethHashrate](#ethhashrate)
        -   [ethMining](#ethmining)
        -   [ethNewBlockFilter](#ethnewblockfilter)
        -   [ethNewFilter](#ethnewfilter)
        -   [ethNewPendingTransactionFilter](#ethnewpendingtransactionfilter)
        -   [ethProtocolVersion](#ethprotocolversion)
        -   [ethSendRawTransaction](#ethsendrawtransaction)
        -   [ethSendTransaction](#ethsendtransaction)
        -   [ethSign](#ethsign)
        -   [ethSubmitWork](#ethsubmitwork)
        -   [ethSyncing](#ethsyncing)
        -   [ethUninstallFilter](#ethuninstallfilter)
        -   [ethPendingTx](#ethpendingtx)
        -   [netListening](#netlistening)
        -   [netPeerCount](#netpeercount)
        -   [netVersion](#netversion)
        -   [shhAddToGroup](#shhaddtogroup)
        -   [shhGetFilterChanges](#shhgetfilterchanges)
        -   [shhGetMessages](#shhgetmessages)
        -   [shhHasIdentity](#shhhasidentity)
        -   [shhNewFilter](#shhnewfilter)
        -   [shhNewGroup](#shhnewgroup)
        -   [shhNewIdentity](#shhnewidentity)
        -   [shhPost](#shhpost)
        -   [shhUninstallFilter](#shhuninstallfilter)
        -   [shhVersion](#shhversion)
        -   [web3ClientVersion](#web3clientversion)
        -   [web3Sha3](#web3sha3)
        
# 概览
> Java SDK是PlatON面向java开发者，提供的PlatON公链的java开发工具包

# 版本说明

## v0.2.0 更新说明

1. 支持PlatON的Sophia智能合约

# 快速入门

## 安装或引入

### 环境要求

1. jdk1.8

### maven
1. 仓库地址 http://sdk.platon.network/content/groups/public/
2. maven方式引用
```
<dependency>
    <groupId>com.platon.client</groupId>
    <artifactId>core</artifactId>
    <version>x.x.x</version>
</dependency>
```
3. gradle方式引用
```
compile "com.platon.client:core:x.x.x"
```
### 合约骨架生成工具
1. 安装包下载 https://github.com/PlatONnetwork/client-sdk-java/releases
2. 解压后说明
```
.
+-- _bin
|   +-- web3j.bat                 //windows执行程序
|   +-- web3j                     //linux执行程序
+-- _lib
|   +-- console-x.x.x-all.jar     //类库
```
3. 到bin目录执行 ./webj

```
              _      _____ _     _
             | |    |____ (_)   (_)
__      _____| |__      / /_     _   ___
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/
                         _/ |
                        |__/

Usage: web3j version|wallet|solidity|truffle|sophia ...
```

## 初始化代码
```
Web3j web3 = Web3j.build(new HttpService("http://localhost:6789"));
```

# 合约

## 合约示例

```
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
```

## 合约骨架生成
1. Sophia智能合约的编写及其ABI(wasm文件)和BIN(json文件)生成方法请参考 [wiki](https://github.com/PlatONnetwork/wiki/wiki)
2. 使用合约骨架生成工具
```
web3j sophia generate /path/to/token.wasm /path/to/token.cpp.abi.json -o /path/to/src/main/java -p com.your.organisation.name
```

## 加载合约
```
Web3j web3j = Web3j.build(new HttpService("http://localhost:6789"));
Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
Token contract = Token.load("0x<address>|<ensName>", web3j, credentials, GAS_PRICE, GAS_LIMIT);
```

## 部署合约
```
Web3j web3 = Web3j.build(new HttpService("http://localhost:6789"));
Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
Token contract = Token.deploy(web3,credentials,GAS_PRICE, GAS_LIMIT,<param1>, ..., <paramN>).send();
```

## 合约call调用
```
Request<?, org.web3j.protocol.core.methods.response.EthCall> req = web3j.ethCall(Transaction.createEthCallTransaction(
               "0xa70e8dd61c5d32be8058bb8eb970870f07233155",
               "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                       "0x0"),
               DefaultBlockParameter.valueOf("latest"));
org.web3j.protocol.core.methods.response.EthCall res = req.send();
String value = res.getValue();

```

## 合约sendRawTransaction调用
```
Web3j web3j = Web3j.build(new HttpService("http://localhost:6789"));

String signedData = "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675";
Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> req = web3j.ethSendRawTransaction(signedData);
org.web3j.protocol.core.methods.response.EthSendTransaction res = req.send();
String transactionHash = res.getTransactionHash();

```
## 合约event
假设合约中定义了名称为transfer的事件，则可以使用下面的代码监听：

```
String contractAddress = "0x223424fskljlsldfsf";
Event event = new Event("transfer", Arrays.asList(new TypeReference<Address>() {}));
EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
        DefaultBlockParameterName.LATEST, contractAddress);
filter.addSingleTopic(EventEncoder.encode(event));
web3j.ethLogObservable(filter).subscribe(log -> {
    System.out.println(log);
});
```

# web3
## web3 eth相关 (标准JSON RPC )

### ethAccounts
>查询当前客户端拥有的地址列表

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthAccounts>`
EthAccounts属性中的accounts即为对应存储数据

**示例**
```
Request<?, EthAccounts> req = web3j.ethAccounts();
EthAccounts res = req.send();
List<String> accounts = res.getAccounts();
```

### ethBlockNumber
>返回最新区块编号

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthBlockNumber>`
EthBlockNumber属性中的blockNumber即为对应存储数据

**示例**
```
Request<?, EthBlockNumber> req = web3j.ethBlockNumber();
EthBlockNumber res = req.send();
BigInteger blockNumber = res.getBlockNumber();
```

### ethCall
>执行消息调用，但不在链上创建交易

**参数**
    
      名称                  类型                    属性   含义
    ----------------------- --------------------- ------ -------
    transaction             Transaction            必需    交易信息
    defaultBlockParameter   DefaultBlockParameter  必需    区块参数

**返回值**

`Request<?, org.web3j.protocol.core.methods.response.EthCall>`
EthCall属性中的value即为对应存储数据

**示例**
```
Request<?, org.web3j.protocol.core.methods.response.EthCall> req = web3j.ethCall(Transaction.createEthCallTransaction(
                                               "0xa70e8dd61c5d32be8058bb8eb970870f07233155",
                                               "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                                                       "0x0"),
                                               DefaultBlockParameter.valueOf("latest"));
org.web3j.protocol.core.methods.response.EthCall res = req.send();
String value = res.getValue();
```

### ethCoinbase
>返回当前块的哈希,seedHash,满足的边界条件

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthCoinbase>`
EthCoinbase属性中的address即为对应存储数据

**示例**
```
Request<?, EthCoinbase> req = web3j.ethCoinbase();
EthCoinbase res = req.send();
String address = res.getAddress();
```

### ethEstimateGas
>查询完成交易所需要使用的Gas数量

**参数**
    
      名称                  类型                    属性   含义
    ----------------------- --------------------- ------ -------
    transaction             Transaction            必需    交易信息

**返回值**

`Request<?, EthEstimateGas>`
EthEstimateGas属性中的amountUsed即为对应存储数据

**示例**
```
Request<?, EthEstimateGas> req = web3j.ethEstimateGas(Transaction.createEthCallTransaction(
                                                             "0xa70e8dd61c5d32be8058bb8eb970870f07233155",
                                                             "0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", "0x0"));
EthEstimateGas res = req.send();
BigInteger used = res.getAmountUsed();
```

### ethGasPrice
>查询当前Gas价格，单位为Wei

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthGasPrice>`
EthGasPrice属性中的gasPrice即为对应存储数据

**示例**
```
Request<?, EthGasPrice> req = web3j.ethGasPrice();
EthGasPrice res = req.send();
BigInteger gasPrice = res.getGasPrice();
```

### ethGetBalance

> 查询指定账户的余额

**参数**

      名称                 类型                  属性    含义
    --------------------- --------------------- ------- -------
    address               String                必需    账户地址
    defaultBlockParameter DefaultBlockParameter 必需    块高类型

**返回值**

`Request<?, EthGetBalance>`

余额对应EthGetBalance对象的balance属性

**示例**
```
Request<?, EthGetBalance> req = web3j.ethGetBalance("0x407d73d8a49eeb85d32cf465507dd71d507100c1",DefaultBlockParameterName.LATEST);
EthGetBalance res = req.send();
BigInteger balance = res.getBalance();
```


### ethGetBlockByHash
>根据区块Hash查询区块信息

**参数**
    
      名称                         类型                 属性    含义
    ---------------------------- --------------------- ------ -------
    blockHash                    DefaultBlockParameter  必需    区块Hash
    returnFullTransactionObjects boolean                必需    是否返回完整的交易信息

**返回值**

`Request<?, EthBlock>`
EthBlock属性中的block即为对应存储数据

**示例**
```
Request<?, EthBlock> req = web3j.ethGetBlockByHash("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331", true);
EthBlock res = req.send();
Block block = res.getBlock();           
```
### ethGetBlockByNumber
>根据区块号查询区块信息

**参数**
    
      名称                         类型                  属性    含义
    ---------------------------- ---------------------- ------ -------
    defaultBlockParameter        DefaultBlockParameter  必需    区块号
    returnFullTransactionObjects boolean                必需    是否返回完整的交易信息

**返回值**

`Request<?, EthBlock>`
EthBlock属性中的block即为对应存储数据

**示例**
```
Request<?, EthBlock> req = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(Numeric.toBigInt("0x1b4")), true);
EthBlock res = req.send();
Block block = res.getBlock();           
```

### ethGetBlockTransactionCountByHash
>根据区块Hash查询交易数

**参数**
    
      名称                         类型                  属性    含义
    ---------------------------- ---------------------- ------ -------
    defaultBlockParameter        DefaultBlockParameter  必需    区块号
    returnFullTransactionObjects boolean                必需    是否返回完整的交易信息

**返回值**

`Request<?, EthGetBlockTransactionCountByHash>`
EthGetBlockTransactionCountByHash属性中的transactionCount即为对应存储数据

**示例**
```
Request<?, EthGetBlockTransactionCountByHash> req = web3j.ethGetBlockTransactionCountByHash("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238");
EthGetBlockTransactionCountByHash res = req.send();
BigInteger count = res.getTransactionCount();
```

### ethGetBlockTransactionCountByNumber
>根据区块号查询交易数

**参数**
    
      名称                         类型                  属性    含义
    ---------------------------- ---------------------- ------ -------
    defaultBlockParameter        DefaultBlockParameter  必需    区块号
    returnFullTransactionObjects boolean                必需    是否返回完整的交易信息

**返回值**

`Request<?, EthGetBlockTransactionCountByNumber>`
EthGetBlockTransactionCountByNumber属性中的transactionCount即为对应存储数据

**示例**
```
Request<?, EthGetBlockTransactionCountByNumber> req = web3j.ethGetBlockTransactionCountByNumber(DefaultBlockParameter.valueOf(Numeric.toBigInt("0xe8")));
EthGetBlockTransactionCountByNumber res = req.send();
BigInteger count = res.getTransactionCount();
```
### ethGetCode
>返回指定地址的代码

**参数**
    
      名称                 类型                  属性    含义
    --------------------- --------------------- ------- -------
    address               String                必需    账户地址
    defaultBlockParameter DefaultBlockParameter 必需    块高类型

**返回值**

`Request<?, EthGetCode>`
EthGetCode属性中的code即为对应存储数据

**示例**
```
Request<?, EthGetCode> req = web3j.ethGetCode("0x295a70b2de5e3953354a6a8344e616ed314d7251", DefaultBlockParameter.valueOf(Numeric.toBigInt("0x2")));
EthGetCode res = req.send();
String code = res.getCode();           
```
### ethGetFilterChanges
>获取过滤器变更信息

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    filterId                BigInteger   必需     过滤器Id

**返回值**

`Request<?, EthLog>`
EthLog属性中的logs即为对应存储数据

**示例**
```
Request<?, EthLog> req = web3j.ethGetFilterChanges(Numeric.toBigInt("0x16"));
EthLog res = req.send();
List<LogResult> logs = res.getLogs();
```

### ethGetFilterLogs
>获取过滤器日志

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    filterId                BigInteger   必需     过滤器Id

**返回值**

`Request<?, EthLog>`
EthLog属性中的logs即为对应存储数据

**示例**
```
Request<?, EthLog> req = web3j.ethGetFilterLogs(Numeric.toBigInt("0x16"));
EthLog res = req.send();
List<LogResult> logs = res.getLogs();
```

### ethGetLogs
>返回符合给定过滤条件的日志数组

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    ethFilter             EthFilter     必需     nonce

**返回值**

`Request<?, EthLog>`
EthLog属性中的logs即为对应存储数据

**示例**
```
Request<?, EthLog> req = web3j.ethGetLogs(new EthFilter().addSingleTopic(
              "0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b"));
EthLog res = req.send();
List<LogResult> logs = res.getLogs();
```

### ethGetStorageAt

> 获取某一地址上指定位置的存储数据

**参数**
    
      名称                 类型                  属性    含义
    --------------------- --------------------- ------- -------
    address               String                必需    账户地址
    position              BigInteger            必需    索引位置
    defaultBlockParameter DefaultBlockParameter 必需    块高类型

**返回值**

`Request<?, EthGetStorageAt>`
EthGetStorageAt属性中的data即为对应存储数据

**示例**
```
Request<?, EthGetStorageAt> req = web3j.ethGetStorageAt("0x295a70b2de5e3953354a6a8344e616ed314d7251", BigInteger.ZERO,
                DefaultBlockParameterName.LATEST);
EthGetStorageAt res = req.send();
String data = res.getData();           
```
### ethGetTransactionByBlockHashAndIndex
>根据区块Hash和交易索引查询交易

**参数**
    
      名称            类型       属性    含义
    ---------------- ----------- ------ -------
    blockHash              String      必需    区块Hash
    transactionIndex       BigInteger  必需    交易索引

**返回值**

`Request<?, EthTransaction>`
EthTransaction属性中的transaction即为对应存储数据

**示例**
```
Request<?, EthTransaction> req = web3j.ethGetTransactionByBlockHashAndIndex(
                                                       "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
                                                       BigInteger.ZERO);
EthTransaction res = req.send();
Optional<Transaction> transaction = res.getTransaction();
```
### ethGetTransactionByBlockNumberAndIndex
>根据区块号和交易索引查询交易

**参数**
    
      名称                 类型                   属性    含义
    ---------------------- --------------------- ------ -------
    defaultBlockParameter  DefaultBlockParameter 必需    区块号
    transactionIndex       BigInteger            必需    交易索引

**返回值**

`Request<?, EthTransaction>`
EthTransaction属性中的transaction即为对应存储数据

**示例**
```
Request<?, EthTransaction> req = web3j.ethGetTransactionByBlockNumberAndIndex(
                                                       DefaultBlockParameter.valueOf(Numeric.toBigInt("0x29c")), BigInteger.ZERO);
EthTransaction res = req.send();
Optional<Transaction> transaction = res.getTransaction();
```

### ethGetTransactionByHash
>根据交易Hash查询交易

**参数**
    
      名称                         类型       属性    含义
    ---------------------------- ----------- ------ -------
    transactionHash              String      必需    交易Hash

**返回值**

`Request<?, EthTransaction>`
EthTransaction属性中的transaction即为对应存储数据

**示例**
```
Request<?, EthTransaction> req = web3j.ethGetTransactionByHash("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238");
EthTransaction res = req.send();
Optional<Transaction> transaction = res.getTransaction();
```

### ethGetTransactionCount
>获取来自指定地址的交易数量

**参数**
    
      名称                  类型                    属性   含义
    ----------------------- --------------------- ------ -------
    address                 String                 必需    地址
    defaultBlockParameter   DefaultBlockParameter  必需    区块参数

**返回值**

`Request<?, EthGetTransactionCount>`
EthGetTransactionCount属性中的transactionCount即为对应存储数据

**示例**
```
Request<?, EthGetTransactionCount> req = web3j.ethGetTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                                   DefaultBlockParameterName.LATEST);
EthGetTransactionCount res = req.send();
BigInteger count = res.getTransactionCount();
```

### ethGetTransactionReceipt
>根据交易Hash查询交易接收者信息

**参数**
    
      名称           类型     属性    含义
    ---------------- ------- ------ -------
    transactionHash  String  必需    交易Hash

**返回值**

`Request<?, EthGetTransactionReceipt>`
EthGetTransactionReceipt属性中的transactionReceipt即为对应存储数据

**示例**
```
Request<?, EthGetTransactionReceipt> req = web3j.ethGetTransactionReceipt("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238");
EthGetTransactionReceipt res = req.send();
Optional<EthGetTransactionReceipt> receipt = res.getTransaction();
```
### ethHashrate
>返回挖矿速度

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthHashrate>`
EthHashrate属性中的hashrate即为对应存储数据

**示例**
```
Request<?, EthHashrate> req = web3j.ethHashrate();
EthHashrate res = req.send();
BigInteger hashRate = res.getHashrate();
```
### ethGetWork
>返回当前块的哈希,seedHash,满足的边界条件

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthGetWork>`
EthGetWork属性数据：
 currentBlockHeaderPowHash
 seedHashForDag
 boundaryCondition

**示例**
```
Request<?, EthLog> req = web3j.ethGetWork();
EthLog res = req.send();
List<LogResult> logs = res.getLogs();
```

### ethMining
>如果客户端在挖矿中，则返回true

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthMining>`
EthMining属性中的isMining即为对应存储数据

**示例**
```
Request<?, EthMining> req = web3j.ethMining();
EthMining res = req.send();
boolean isMining = res.isMining();
```
### ethNewBlockFilter
>创建新用于监听新块的过滤器对象

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthFilter>`
EthFilter属性中的filterId即为对应过滤器Id

**示例**
```
Request<?, EthFilter> req = web3j.ethNewBlockFilter();
EthFilter res = req.send();
String filterId = res.getFilterId();
```
### ethNewFilter
>创建新的过滤器对象

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    ethFilter             EthFilter     必需     过滤器对象

**返回值**

`Request<?, EthFilter>`
EthFilter属性中的filterId即为对应过滤器Id

**示例**
```
EthFilter ethFilter = new EthFilter().addSingleTopic("0x12341234");
Request<?, EthFilter> req = web3j.ethNewFilter(ethFilter);
EthFilter res = req.send();
String filterId = res.getFilterId();
```
### ethNewPendingTransactionFilter
>创建用于监听新待处理交易的过滤器对象

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthFilter>`
EthFilter属性中的filterId即为对应过滤器Id

**示例**
```
Request<?, EthFilter> req = web3j.ethNewPendingTransactionFilter();
EthFilter res = req.send();
String filterId = res.getFilterId();
```

### ethProtocolVersion
>返回当前的以太坊协议版本

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthProtocolVersion>`
EthProtocolVersion属性中的protocolVersion即为对应存储数据

**示例**
```
Request<?, EthProtocolVersion> req = web3j.ethProtocolVersion();
EthProtocolVersion res = req.send();
String protocolVersion = res.getProtocolVersion();
```
### ethSendRawTransaction
>为已签名交易数据发起交易或调用合约

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
    signedTransactionData   String         必需    已签名的交易数据

**返回值**

`Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>`
EthSendTransaction属性中的transactionHash即为对应存储数据

**示例**
```
Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> req = web3j.ethSendRawTransaction(
                                       "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f"
                                               + "072445675058bb8eb970870f072445675");
org.web3j.protocol.core.methods.response.EthSendTransaction res = req.send();
String transactionHash = res.getTransactionHash();
```

### ethSendTransaction
>发送交易

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
    signedTransactionData   String         必需    已签名的交易数据

**返回值**

`Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction>`
EthSendTransaction属性中的transactionHash即为对应存储数据

**示例**
```
Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> req = web3j.ethSendTransaction(new Transaction(
                                    "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                                    BigInteger.ONE,
                                    Numeric.toBigInt("0x9184e72a000"),
                                    Numeric.toBigInt("0x76c0"),
                                    "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                                    Numeric.toBigInt("0x9184e72a"),
                                    "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb"
                                    + "970870f072445675058bb8eb970870f072445675"));
org.web3j.protocol.core.methods.response.EthSendTransaction res = req.send();
String transactionHash = res.getTransactionHash();
```
### ethSign
>对数据签名

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    address                 String      必需    地址
    sha3HashOfDataToSign    String      必需    被签名数据的sha3Hash码

**返回值**

`Request<?, EthSign>`
EthSign属性中的signature即为对应存储数据

**示例**
```
Request<?, EthSign> req = web3j.ethSign("0x8a3106a3e50576d4b6794a0e74d3bb5f8c9acaab",
                               "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470");
EthSign res = req.send();
String signature = res.getSignature();
```
### ethSubmitWork
>提交POW工作量

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    nonce                 String      必需     nonce
    headerPowHash         String      必需     pow-hash
    mixDigest             String      必需     Digest

**返回值**

`Request<?, EthSubmitWork>`
EthSubmitWork属性中的solutionValid即为对应存储数据

**示例**
```
Request<?, EthSubmitWork> req = web3j.ethSubmitWork("0x0000000000000001",
                "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef",
                "0xD1FE5700000000000000000000000000D1FE5700000000000000000000000000");
EthSubmitWork res = req.send();
boolean valid = res.solutionValid();
```
### ethSyncing
>返回带有同步状态数据的对象

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
                            无参数

**返回值**

`Request<?, EthSyncing>`
EthSyncing对象携带了同步状态数据

**示例**
```
Request<?, EthSyncing> req = web3j.ethSyncing();
EthSyncing res = req.send();
```
### ethUninstallFilter
>删除过滤器对象

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
    filterId                BigInteger  必需     过滤器Id

**返回值**

`Request<?, EthUninstallFilter>`
EthUninstallFilter属性中的isUninstalled即为对应存储数据

**示例**
```
Request<?, EthUninstallFilter> req = web3j.ethUninstallFilter(Numeric.toBigInt("0xb"));
EthUninstallFilter res = req.send();
boolean isUninstalled = res.isUninstalled();
```
### ethPendingTx
>查询待处理交易

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                         无参数

**返回值**

`Request<?, EthPendingTransactions>`
EthPendingTransactions属性中的transactions即为对应存储数据

**示例**
```
Request<?, EthPendingTransactions> req = web3j.ethPendingTx();
EthPendingTransactions res = req.send();
List<Transaction> transactions = res.getTransactions();
```

### netListening
>查看客户端是否在监听网络连接

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                         无参数

**返回值**

`Request<?, NetListening>`
NetListening属性中的isListening表示客户端是否在监听网络连接

**示例**
```
Request<?, NetListening> req = web3j.netListening();
NetListening res = req.send();
boolean isListening = res.isListening();
```
### netPeerCount
>查看连接到当前客户端的节点数

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                         无参数

**返回值**

`Request<?, NetPeerCount>`
NetPeerCount属性中的quantity表示节点数

**示例**
```
Request<?, NetPeerCount> req = web3j.netPeerCount();
NetPeerCount res = req.send();
BigInteger isListening = res.getQuantity();
```

### netVersion
>查看当前网络Id

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                         无参数

**返回值**

`Request<?, NetVersion>`
NetPeerCount属性中的netVersion表示版本信息

**示例**
```
Request<?, NetVersion> req = web3j.netVersion();
NetVersion res = req.send();
String version = res.getNetVersion();
```


### shhAddToGroup
>Adds a whisper identity to the group.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
     address               String                必需    账户地址

**返回值**

`Request<?, ShhAddToGroup>`
ShhAddToGroup属性中的addedToGroup表示是否添加成功

**示例**
```
Request<?, ShhAddToGroup> req = web3j.shhAddToGroup("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1");
ShhAddToGroup res = req.send();
boolean isSuccess = res.addedToGroup();
```

### shhGetFilterChanges
>Polling method for whisper filters. Returns new messages since the last call of this method.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
     filterId               BigInteger      必需  过滤器Id

**返回值**

`Request<?, ShhMessages>`
ShhMessages属性中的messages表示消息数据

**示例**
```
Request<?, ShhMessages> req = web3j.shhGetFilterChanges(Numeric.toBigInt("0x7"));
ShhMessages res = req.send();
List<SshMessage> messages = res.getMessages();
```

### shhGetMessages
>Get all messages matching a filter. Unlike shh_getFilterChanges this returns all messages.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
     filterId               BigInteger      必需  过滤器Id

**返回值**

`Request<?, ShhMessages>`
ShhMessages属性中的messages表示消息数据

**示例**
```
Request<?, ShhMessages> req = web3j.shhGetMessages(Numeric.toBigInt("0x7"));
ShhMessages res = req.send();
List<SshMessage> messages = res.getMessages();
```

### shhHasIdentity
>检查客户端是否持有给定ID的私钥

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
     identityAddress        String          必需  Id

**返回值**

`Request<?, ShhHasIdentity>`
ShhHasIdentity属性中的hasPrivateKeyForIdentity表示所指定标识是否有私钥

**示例**
```
Request<?, ShhHasIdentity> req = web3j.shhHasIdentity("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1");
ShhHasIdentity res = req.send();
boolean isHas = res.hasPrivateKeyForIdentity();
```

### shhNewFilter
>Creates filter to notify, when client receives whisper message matching the filter options.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
     shhFilter              ShhFilter      必需    Shh过滤器

**返回值**

`Request<?, ShhNewFilter>`
ShhNewFilter属性中的filterId表示创建的过滤器Id

**示例**
```
Request<?, ShhNewFilter> req = web3j.shhNewFilter(
                   new ShhFilter("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1")
                   .addSingleTopic("0x12341234bf4b564f"));
ShhNewFilter res = req.send();
BigInteger filterId = res.getFilterId();
```

### shhNewGroup
>创建新的群组

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                            无参数

**返回值**

`Request<?, ShhNewGroup>`
ShhNewGroup属性中的address表示群组地址

**示例**
```
Request<?, ShhNewGroup> req = web3j.shhNewGroup();
ShhNewGroup res = req.send();
String address = res.getAddress();
```

### shhNewIdentity
>Creates new whisper identity in the client.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                            无参数

**返回值**

`Request<?, ShhNewIdentity>`
ShhNewIdentity属性中的address表示地址

**示例**
```
Request<?, ShhNewIdentity> req = web3j.shhNewIdentity();
ShhNewIdentity res = req.send();
String address = res.getAddress();
```

### shhPost
>Sends a whisper message.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
    ShhPost                 shhPost        必需    参数封装对象

**返回值**

`Request<?, org.web3j.protocol.core.methods.response.ShhPost>`
ShhPost属性中的messageSent表示消息发送结果

**示例**
```
Request<?, org.web3j.protocol.core.methods.response.ShhPost> req = web3j.shhPost(new ShhPost(
                            "0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1",
                            "0x3e245533f97284d442460f2998cd41858798ddf04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a0d4d661997d3940272b717b1",
                            Arrays.asList("0x776869737065722d636861742d636c69656e74", "0x4d5a695276454c39425154466b61693532"),
                            "0x7b2274797065223a226d6",
                            Numeric.toBigInt("0x64"),
                            Numeric.toBigInt("0x64")));
org.web3j.protocol.core.methods.response.ShhPost res = req.send();
boolean sent = res.messageSent();
```

### shhUninstallFilter
>Uninstalls a filter with given id. Should always be called when watch is no longer needed. Additonally Filters timeout when they aren't requested with shh_getFilterChanges for a period of time.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
    filterId               BigInteger      必需  过滤器Id

**返回值**

`Request<?, ShhUninstallFilter>`
ShhUninstallFilter属性中的isUninstalled表示卸载结果

**示例**
```
Request<?, ShhUninstallFilter> req = web3j.shhUninstallFilter(Numeric.toBigInt("0x7"));
ShhUninstallFilter res = req.send();
boolean isUninstalled = res.isUninstalled();
```

### shhVersion
>Uninstalls a filter with given id. Should always be called when watch is no longer needed. Additonally Filters timeout when they aren't requested with shh_getFilterChanges for a period of time.

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                            无参数

**返回值**

`Request<?, ShhVersion>`
ShhVersion属性中的version表示版本信息

**示例**
```
Request<?, ShhVersion> req = web3j.shhVersion();
ShhVersion res = req.send();
String version = res.getVersion();
```

### web3ClientVersion
>查看当前客户端版本

**参数**
    
      名称                  类型            属性   含义
    ----------------------- -------------- ------ -------
                         无参数

**返回值**

`Request<?, Web3ClientVersion>`
Web3ClientVersion属性中的web3ClientVersion表示版本信息

**示例**
```
Request<?, Web3ClientVersion> req = web3j.web3ClientVersion();
Web3ClientVersion res = req.send();
String version = res.getWeb3ClientVersion();
```

### web3Sha3
>计算指定数据的Keccak-256 hash值

**参数**
    
      名称                  类型         属性   含义
    ----------------------- ----------- ------ -------
      data                  String      必需    转换为sha3 hash的数据

**返回值**

`Request<?, Web3Sha3>`
Web3Sha3属性中的result表示sha3 hash

**示例**
```
Request<?, Web3Sha3> req = web3j.web3Sha3("0x68656c6c6f20776f726c64");
Web3Sha3 res = req.send();
String sha3 = res.getResult();
```