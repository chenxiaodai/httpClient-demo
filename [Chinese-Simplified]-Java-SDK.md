-   [概览](#概览)
-   [版本说明](#版本说明)
    -   [v0.2.0 更新说明](#v0.2.0-更新说明)
    -   [v0.3.0 更新说明](#v0.3.0-更新说明)
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
    -   [内置合约](#内置合约)
        -  [CandidateContract](#CandidateContract)
-   [web3](#web3)
    -   [web3 eth相关 (标准JSON RPC )](#web3-eth相关-标准json-rpc)
    -   [新增的接口](#新增的接口)
        -   [ethPendingTx](#ethpendingtx)

# 概览
> Java SDK是PlatON面向java开发者，提供的PlatON公链的java开发工具包

# 版本说明

## v0.2.0 更新说明
1. 支持PlatON的智能合约

## v0.3.0 更新说明
1. 实现了PlatON协议中交易类型定义
2. 优化了骨架生成，增加合约部署和调用过程中gaslimit的评估方法 
3. 优化了默认gasLimit、gasPrice的值
4. 增加内置合约CandidateContract

# 快速入门

## 安装或引入

### 环境要求
1. jdk1.8

### maven
1. 仓库地址 https://sdk.platon.network/nexus/content/groups/public/
    
    1. maven项目配置
    ```
    <repository>
	    <id>platon-public</id>
	    <url>https://sdk.platon.network/nexus/content/groups/public/</url>
	</repository>
    ```

    2. gradle项目配置
    ```
    repositories {
        maven { url "https://sdk.platon.network/nexus/content/groups/public/" }
    }
    ```

2. maven方式引用
```
<dependency>
    <groupId>com.platon.client</groupId>
    <artifactId>core</artifactId>
    <version>0.3.0</version>
</dependency>
```
3. gradle方式引用
```
compile "com.platon.client:core:0.3.0"
```
### 合约骨架生成工具
1. 安装包下载 https://download.platon.network/client-sdk.zip
2. 解压后说明
```
.
+-- _bin
|   +-- client-sdk.bat                 //windows执行程序
|   +-- client-sdk                     //linux执行程序
+-- _lib
|   +-- xxx.jar                        //类库
|   +-- ...
```
3. 到bin目录执行 ./client-sdk

```
              _      _____ _     _
             | |    |____ (_)   (_)
__      _____| |__      / /_     _   ___
\ \ /\ / / _ \ '_ \     \ \ |   | | / _ \
 \ V  V /  __/ |_) |.___/ / | _ | || (_) |
  \_/\_/ \___|_.__/ \____/| |(_)|_| \___/
                         _/ |
                        |__/

Usage: client-sdk version|wallet|solidity|truffle|wasm ...
```

## 初始化代码
```
Web3j web3 = Web3j.build(new HttpService("http://localhost:6789"));
```

# 合约

## 合约示例

```
#include <stdlib.h>
#include <string.h>
#include <string>
#include <platon/platon.hpp>

namespace demo {
    class FirstDemo : public platon::Contract
    {
        public:
            FirstDemo(){}
			
            /// 实现父类: platon::Contract 的虚函数
            /// 该函数在合约首次发布时执行，仅调用一次
            void init() 
            {
                platon::println("init success...");
            }

            /// 定义Event.
            PLATON_EVENT(Notify, uint64_t, const char *)

        public:
            void invokeNotify(const char *msg)
            {	
                // 定义状态变量
                platon::setState("NAME_KEY", std::string(msg));
                // 日志输出
                platon::println("into invokeNotify...");
                // 事件返回
                PLATON_EMIT_EVENT(Notify, 0, "Insufficient value for the method.");
            }

            const char* getName() const 
            {
                std::string value;
                platon::getState("NAME_KEY", value);
                // 读取合约数据并返回
                return value.c_str();
            }
    };
}

// 此处定义的函数会生成ABI文件供外部调用
PLATON_ABI(demo::FirstDemo, invokeNotify)
PLATON_ABI(demo::FirstDemo, getName)

```

## 合约骨架生成
1. wasm智能合约的编写及其ABI(wasm文件)和BIN(json文件)生成方法请参考 [wiki](https://github.com/PlatONnetwork/wiki/wiki)
2. 使用合约骨架生成工具
```
client-sdk wasm generate /path/to/firstdemo.wasm /path/to/firstdemo.cpp.abi.json -o /path/to/src/main/java -p com.your.organisation.name
```

## 加载合约
```
Web3j web3j = Web3j.build(new HttpService("http://localhost:6789"));
Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

byte[] dataBytes = Files.readBytes(new File("<wasm file path>"));
String binData = Hex.toHexString(dataBytes);

Firstdemo contract = Firstdemo.load(binData, "0x<address>", web3j, credentials, new DefaultWasmGasProvider());
```

## 部署合约
```
Web3j web3j = Web3j.build(new HttpService("http://localhost:6789"));
Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

byte[] dataBytes = Files.readBytes(new File("<wasm file path>"));
String binData = Hex.toHexString(dataBytes);

Firstdemo contract = Firstdemo.deploy(web3j, credentials, binData, new DefaultWasmGasProvider()).send();
```

## 合约call调用
```
//abi中方法描述	constant=true 的生成的骨架方法里面调用的是call查询
String name = contract.getName().send();
System.out.println(name);
```

## 合约sendRawTransaction调用
```
//abi中方法描述	constant=false 的生成的骨架方法里面调用的是sendRawTransaction
TransactionReceipt transactionReceipt =  contract.invokeNotify("hello").send();

//如果合约方法中有event，可以通过下面方法获得event内容
List<NotifyEventResponse> eventResponses = contract.getNotifyEvents(transactionReceipt);
for(NotifyEventResponse r:eventResponses) {
    System.out.println(r.param1);
    System.out.println(r.param2);
}

```

## 内置合约
###  CandidateContract
> PlatOn经济模型中候选人相关的合约接口 [合约描述](https://note.youdao.com/)

#### 加载合约
```
Web3j web3j = Web3j.build(new HttpService("http://localhost:6789"));
Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

CandidateContract contract = CandidateContract.load(web3j, credentials, new DefaultWasmGasProvider());
```

#### **`CandidateDeposit`**
> 节点候选人申请/增加质押

入参：
* `nodeId`: [64]byte 节点ID(公钥)
* `owner`: [20]byte 质押金退款地址
* `fee`: uint32 出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
* `host`: string 节点IP
* `port`: string 节点端口号
* `Extra`: string 附加数据(有长度限制，限制值待定) {
sdsd
cdsc
}
* `port`: string 节点端口号

出参（事件：CandidateDepositEvent）：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息

合约方法
```
//节点id
String nodeId = "0x1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429"; 
//质押金退款地址
String owner = "0x493301712671ada506ba6ca7891f436d29185821";
//出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
BigInteger fee =  BigInteger.valueOf(500L);
//节点IP
String host = "127.0.0.1";
//节点P2P端口号
String port = "16789";
//附加数据
JSONObject extra = new JSONObject();
//节点名称
extra.put("nodeName", "xxxx noedeName");
//节点logo
extra.put("nodePortrait", "xxxx nodePortrait");
//机构简介
extra.put("nodeDiscription", "xxxx nodeDiscription");
//机构名称
extra.put("nodeDepartment", "xxxx nodeDepartment");  
//官网
extra.put("officialWebsite", "xxxx officialWebsite");  
  
//调用接口
  TransactionReceipt receipt = contract.CandidateDeposit(nodeId, owner, fee, host, port, extra.toJSONString()).send();
logger.debug("TransactionReceipt:{}", JSON.toJSONString(receipt));

//查看返回event
List<CandidateDepositEventEventResponse>  events = contract.getCandidateDepositEventEvents(receipt);
for (CandidateDepositEventEventResponse event : events) {
	 logger.debug("event:{}", JSON.toJSONString(event));
}
```

#### **`CandidateApplyWithdraw`**
节点质押金退回申请，申请成功后节点将被重新排序，权限校验from==owner。
入参：
* `nodeId`: [64]byte 节点ID(公钥)
* `withdraw`: uint256 退款金额 (单位：ADP)

出参（事件：CandidateApplyWithdrawEvent）：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息

### **`CandidateWithdrawInfos`**
获取节点申请的退款记录列表
入参：
* `nodeId`: [64]byte 节点ID(公钥)

出参：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息
* `[]`:列表
	* 'Balance': uint256 退款金额 (单位：ADP)
	* `LockNumber`: uint256 退款申请所在块高
	* `LockBlockCycle`: uint256 退款金额锁定周期

### **`CandidateWithdraw`**
节点质押金提取，调用成功后会提取所有已申请退回的质押金到owner账户。
入参：
* `nodeId`: [64]byte 节点ID(公钥)

出参（事件：CandidateWithdrawEvent）：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息

### **`SetCandidateExtra`**
设置节点附加信息，供前端扩展使用。
入参：
* `nodeId`: [64]byte 节点ID(公钥)
* `extra`: string 附加信息

出参（事件：SetCandidateExtraEvent）：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息

### **`CandidateDetails`**
获取候选人信息。
入参：
* `nodeId`: [64]byte 节点ID(公钥)

出参：
* `Deposit`: uint256 质押金额 (单位：ADP)
* `BlockNumber`: uint256 质押金更新的最新块高
* `Owner`: [20]byte 质押金退款地址
* `TxIndex`: uint32 所在区块交易索引
* `CandidateId`: [64]byte 节点Id(公钥)
* `From`: [20]byte 最新质押交易的发送方
* `Fee`: uint64 出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
* `Host`: string 节点IP
* `Port`: string 节点端口号
* `Extra`: string 附加数据(有长度限制，限制值待定)

### **`CandidateList`**
获取所有入围节点的信息列表
入参：
* 无

出参：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息
* `[]`:列表
	* `Deposit`: uint256 质押金额 (单位：ADP)
	* `BlockNumber`: uint256 质押金更新的最新块高
	* `Owner`: [20]byte 质押金退款地址
	* `TxIndex`: uint32 所在区块交易索引
	* `CandidateId`: [64]byte 节点Id(公钥)
	* `From`: [20]byte 最新质押交易的发送方
	* `Fee`: uint64 出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
	* `Host`: string 节点IP
	* `Port`: string 节点端口号
	* `Extra`: string 附加数据(有长度限制，限制值待定)

### **`VerifiersList`**
获取参与当前共识的验证人列表
入参：
* 无

出参：
* `Ret`: bool 操作结果
* `ErrMsg`: string 错误信息
* `[]`:列表
	* `Deposit`: uint256 质押金额 (单位：ADP)
	* `BlockNumber`: uint256 质押金更新的最新块高
	* `Owner`: [20]byte 质押金退款地址
	* `TxIndex`: uint32 所在区块交易索引
	* `CandidateId`: [64]byte 节点Id(公钥)
	* `From`: [20]byte 最新质押交易的发送方
	* `Fee`: uint64 出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
	* `Host`: string 节点IP
	* `Port`: string 节点端口号
	* `Extra`: string 附加数据(有长度限制，限制值待定)




# web3
## web3 eth相关 (标准JSON RPC )
- java api的使用请参考[web3j github](https://github.com/web3j/web3j)

## 新增的接口
### ethPendingTx
>查询待处理交易

**参数**
 
 无
 
**返回值**

`Request<?, EthPendingTransactions>`
EthPendingTransactions属性中的transactions即为对应存储数据

**示例**
```
Request<?, EthPendingTransactions> req = web3j.ethPendingTx();
EthPendingTransactions res = req.send();
List<Transaction> transactions = res.getTransactions();
```