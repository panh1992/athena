# API 文档

## 服务入口

&emsp;&emsp;&emsp;&emsp;服务地址为: http://localhost:8080

## 请求认证

&emsp;&emsp;&emsp;&emsp;在首次登录时，会向调用方返回一个下发的token, 该token值将作为后续调用的身份认证。
token本身具有时效性，在超时之后需要重新登录获取新的token。

&emsp;&emsp;&emsp;&emsp;token将作为header里面的 Authorization 字段的值随着请求传输到服务端用于身份认证。

## 公共请求头
&emsp;&emsp;&emsp;&emsp;Athena API是基于HTTP协议的REST风格接口，它支持一组可以在所有API请求中使用的公共请求头，其详细定义如下：

|Header名称 |类型 |说明 |
|:---------|:---|----:|
|Authorization |String |请求认证token，需认证接口必传|

## 通用错误码

&emsp;&emsp;&emsp;&emsp;当API请求发生错误的时候，服务端会返回错误信息，
 包括HTTP的Status、Code和响应Body的具体错误细节。其中响应Body中的错误细节格式如下:

|HTTP状态码 |错误码 |描述 |
|:---------|:----|----:|
|400 |InvalidParameter |请求参数错误 |
|401 |AuthorizationFailed |请求认证失败，不合法的 Token或 Token超时 |
|404 |EntityNotExist |请求获取的资源不存在 |
|409 |EntityAlreadyExists |请求创建的资源已存在 |
|500 |InternalServerError |服务器内部错误 |
