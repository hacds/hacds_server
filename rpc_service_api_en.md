
# Exchange RPC API

Documentation

## 1、

## This document includes the calling specifications and

examples of the hacds diamond weight ratio, diamond information corresponding
to the user's address, and the number of diamonds interface.

## 2. Deployment and Installation:

**1.Install MySQL database version 5.5 or
above.**

**2.Execute the table creation statement
in MySQL -- createsql.txt**

**3.Configuration file instructions
application.yaml:server:

  port: 81  ##Access port

  servlet:

    context-path: /hacds  ## Access prefix**

**datasource:

    mysql:

    driver-class-name:
com.mysql.jdbc.Driver

    url:
jdbc:mysql://${MYSQLIP:127.0.0.1:3306}/${MYSQLNAME:score}?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true  ### Database address and scope**

**    username: root  ## Database username

    username: root  ## Database username

    password: root  ## Database password

    type:
com.alibaba.druid.pool.DruidDataSource**

 **1、** **Start
the program java -jar xxx.jar**

## 3.Interface Call Examples

### 1、Get all

hacds diamond weight ratios

Url：POST: [http://IP:PORT/hacds/diamond/queryDiamondsHacds](http://IP:PORT/hacds/diamond/queryDiamondsHacds)

    Return Example:

{

  "resultCode": 0,

  "data": {

    "hacds": 847300000000

  },

  "resultMsg": "success"

}

### 2、Get the

number of diamonds

URL：GET:http://IP:PORT/hacds/diamond/queryDiamondTotal

 **Return Example** :92176

### 3、Get hacds

diamonds by user address

 URL：POST[http://IP:PORT/hacds/diamond/queryDiamondByAddress](http://IP:PORT/hacds/diamond/queryDiamondByAddress)

 **Input
Parameter** ：{"address":"1LagopcgubBRVEDaf8sVC2ZXFkTFG3H1hZ"}

 **Return
Parameters** ：

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
### 4、Get address all hacds

hacds diamond weight ratios

Url：POST: [http://IP:PORT/hacds/diamond/queryAddressHacds/1QDc1twwVy3acuftAv3GuNnKwxopYi9VLb](http://IP:PORT/hacds/diamond/queryAddressHacds/1QDc1twwVy3acuftAv3GuNnKwxopYi9VLb)

    Return Example:

{

"resultCode": 0,

"data": {

    "hacds": 847300000000

},

"resultMsg": "success"

}
