
# 交易所RPC API 文档

## 一、此文档包含hacds钻石权重比例、用户地址对应钻石信息、钻石数量接口的调用规范及示例。

## 二、部署安装：

 **1、** **安装mysql 5.5****版本以上的数据库**

 **2、** **在mysql****中执行建表语句--createsql.txt**

 **3、** **配置文件说明 application.yaml**

 **server:

  port: 81  ##**  **访问端口

  servlet:

    context-path: /hacds  ##****访问前缀**

 **datasource:

    mysql:

    driver-class-name:
com.mysql.jdbc.Driver

    url:
jdbc:mysql://${MYSQLIP:127.0.0.1:3306}/${MYSQLNAME:score}?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true  ##**  **数据库地址及作用域

    username: root  ##**  **数据库账号

    password: root  ##** **数据库密码

    type:
com.alibaba.druid.pool.DruidDataSource**

 **4、** **启动程序 java -jar xxx.jar**

## 三、接口调用示例

### 1、获取所有钻石hacds权重比例

Url：POST: [http://IP:PORT/hacds/diamond/queryDiamondByAddress](http://IP:PORT/hacds/diamond/queryDiamondByAddress)

    返回示例：

{

  "resultCode": 0,

  "data": {

    "hacds": 847300000000

  },

  "resultMsg": "success"

}

### 2、获取钻石数量

URL：GET:http://IP:PORT/hacds/diamond/queryDiamondTotal

返回示例：92176

### 3、通过用户地址获取hacds钻石

 URL：POST[http://IP:PORT/hacds/diamond/queryDiamondByAddress](http://IP:PORT/hacds/diamond/queryDiamondByAddress)

入参：{"address":"1LagopcgubBRVEDaf8sVC2ZXFkTFG3H1hZ"}

返回参数：

{

  "data": [

    {

    "life_gene": "66604bcc0177cc5878ee99046aaa112833a0dd33e3e5675d527dd2ef40a8a50f",

    "address": "1QDc1twwVy3acuftAv3GuNnKwxopYi9VLb",

    "color": "",

    "average_burn_price": "ㄜ10:248",

    "approx_fee_offer": "ㄜ2:244",

    "inscriptions": "",

    "numberliteral": "",

    "block_height": "263845",

    "current_block_hash": "000000000414b774b0ece6fcadc581c286711945ca2e4bd334719ccb3d4a2d91",

    "nonce": "00000000f5fd1f00",

    "visual_gene": "0fba51f6357d2d2f0850",

    "miner_address": "1BPSG7kfAcZhxb766nngZ93mdMkDeRiis6",

    "number": "36083",

    "prev_block_hash": "00000000022b11bd0afb9c0545b4e4f96388a0e8024465df2fa1be3cab757e79",

    "score": 0,

    "custom_message": "3abeb437b541e0abb62c9e1f884c72f9abdf4996ad86023309c2f62d35052b47",

    "groupName": "",

    "nameliteral": "",

    "price": 0.0,

    "shapes": "",

    "name": "KEATNH",

    "styles": "",

    "status": "1"

    }]}
