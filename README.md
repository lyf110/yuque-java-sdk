# yuque-java-sdk
基于语雀官方API封装的Java版SDK

## 1 语雀官方开发者文档

[语雀开发者文档](https://www.yuque.com/yuque/developer)
⚠️ 注意事项：语雀开放 API 仅用于对语雀内容的正常读取，不允许把语雀当做内容存储平台，请合理使用语雀开放 API。系统识别异常行为后会进行屏蔽处理，可能会导致用户不可用等情形。

### 1.1 基本路径

所有 API 的路径都以 `https://www.yuque.com/api/v2` 开头。
访问空间内资源的 API 需要使用空间对应的域名，例如空间域名为 `customspace.yuque.com`， 则 API 的基础路径为 `https://customspace.yuque.com/api/v2`。

建议开启 follow redirect 能力:
```bash
# -L To follow redirect with Curl
curl -L -X "POST" "https://www.yuque.com/api/v2/..." \
     -H 'User-Agent: your_name' \
     -H 'X-Auth-Token: your_token' \
     -H 'Content-Type: application/json' \
     -d $'{}'
```

### 1.2 HTTP Verbs

| Verb | Description |
| --- | --- |
| GET | 用于获取数据 |
| POST | 用于创建数据 |
| PUT | 用于修改部分数据，例如一个文档标题，正文 |
| DELETE | 用于删除数据 |


### 1.4 HTTP 提交数据说明

当 **POST**, **PUT** 请求的时候，请确保 Request Content-Type 是 `application/json` 类型。

```json
req.Headers.Add("Content-Type", "application/json")
```


### 1.5 User-Agent Header

为了确保我们能知道访问者是谁，API 要求必须传递 `User-Agent` Header，否则将会拒绝请求。

例如:
```go
req.Headers.Add("User-Agent", "这里可以填应用名称")
```

### 1.6 身份认证


语雀所有的开放 API 都需要 Token 验证之后才能访问。

语雀 API 目前使用 Token 机制来实现身份认证。

你需要在请求的 HTTP Headers 传入 `X-Auth-Token` 带入您的身份 Token 信息，用于完成认证。

#### 1.6.1 个人用户认证
获取 Token 可通过点击语雀的个人头像，并进入 [个人设置](/settings/tokens) 页面拿到，如下图：
![image.png](https://cdn.nlark.com/yuque/0/2023/png/84151/1680345283904-509c590e-b0b3-45a6-a9ff-aedde629b0c9.png)


**For example**
```bash
curl -H "X-Auth-Token: gCmkIlgAtuc3vFwpLfeM1w==" https://www.yuque.com/api/v2/hello
```

**Response**
```json
{
  "data":{
    "message":"Hello 小明"
  }
}
```

`X-Auth-Token` 依据用户有的权限，决定了能获取到的数据，例如，假如 “小明” 这个账号是 “[语雀/帮助](/yuque/help)” 这个文档仓库的 `Owner`，那么通过他的 Token 可以获取到这个仓库的所有信息。

其他情况由具体的功能权限设定来决定能获取到什么样的数据，以及那些数据有修改权限，详见后面 API 的具体接口返回的 `abilities` 描述。

#### 1.6.2 企业团队身份认证

> 即团队token

空间内的团队，可进入团队设置页面进行获取（仅[旗舰版空间](https://www.yuque.com/about/price)可使用）。
![image.png](https://cdn.nlark.com/yuque/0/2023/png/84151/1680345436472-0dce9c77-252c-4154-b562-522f43e845b8.png#averageHue=%23fcfcfc&clientId=u1fc313b9-a9c1-4&from=paste&height=551&id=uc43973f9&originHeight=1206&originWidth=2338&originalType=binary&ratio=2&rotation=0&showTitle=false&size=109287&status=done&style=shadow&taskId=u26c0e0e8-8d39-4a1a-b0df-3e7ee31ea70&title=&width=1068)

**For example**
```bash
curl -H "X-Auth-Token: gCmkIlgAtuc3vFwpLfeM1w==" https://customspace.yuque.com/api/v2/hello
```

**Response**
```json
{
  "data":{
    "message":"Hello xx 团队"
  }
}
```

通过 `X-Auth-Token` ，语雀能够识别到当前访问的是哪个团队，可获取到该团队内的知识库、文档、以及成员等相关的数据。


### 1.7 HTTP 状态码

- 200 - 成功
- 400 - 请求的参数不正确，或缺少必要信息，请对比文档
- 401 - 需要用户认证的接口用户信息不正确
- 403 - 缺少对应功能的权限
- 404 - 数据不存在，或未开放
- 500 - 服务器异常

### 1.8 参数说明
| Name | Description | Example |
| --- | --- | --- |
| id | 数据的唯一编号/主键 | 1984 |
| login | 用户／团队的唯一名称<br/>用户／团队编号 |用户：用户个人路径<br/>团队：如[语雀团队](/yuque)，login 值为 `yuque`|
| book_slug | 仓库唯一名称 | 如[语雀开发者文档](/yuque/developer)这个仓库，`book_slug` 值为 `developer`** ** |
| namespace | 仓库的唯一名称<br/>需要组合 `:login/:book_slug`<br/>或可以直接使用仓库编号 |`yuque/developer`|
| slug | 文档唯一名称 | 如[当前这篇文档](/yuque/developer/api)的 slug 值为   `api` |

### 1.9 返回数据格式

- JSON 格式

```json
{
  "data": {
    "id": 10,
    "slug": "weekly",
    "name": "技术周刊",
    "abilities": {
      "update": false,
      "destroy": false
    }
  },
  "meta": {
    "liked": false,
    "followed": false,
  }
}
```

- id: 每个数据都会有的，Resource 的唯一编号，后续很多地方你可能需要用它查询
- abilities: 表述当前登陆者对于此资源的权限
- meta: 一些附加信息，例如是否赞过，是否关注过

### 1.10 Rate Limit 访问频率限制

- 每个用户/团队，其下所有 Token 共享 RateLimit，5000 次/小时

每次请求 `Response Header` 将会返回频率限制的信息，例如：

```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 75
```

- `X-RateLimit-Limit` - 总次数
- `X-RateLimit-Remaining` - 剩余次数

如果超过限制，将会返回:

```
HTTP/1.1 429 Too Many Requests
```

### 1.11 DateTime 格式
DateTime 使用 [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) 标准格式，请按照标准方式进行转换。