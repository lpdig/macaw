AB实验分流算法包
---
## 摘要
这是一个实现了AB实验流量分配算法的Java包。
主要实现了如下功能：
* 支持多层实验配置
* 实现按照key哈希的实验选择方法， 能够保证不同层的实验策略正交组合。
* Thompson Sampling 算法实现的实验策略选择方法。大部分流量会分配到目前为止表现最好的策略，少部分流量会分配到其它策略进行实验。
* UCB算法实现的实验策略选择方法。如果有新策略则选择新策略，否则选择置信区间上界最大的那个策略。
* Epsilon greedy算法实现的策略选择方法。

## 基本概念
* 实验策略（policy）: 由一个策略的名称和一组参数构成。策略在此包中是个抽象的概念，它实际对应的是业务系统的某种实验策略，可以是某种实体，也可以是一组实验参数(比如推荐算法的某种策略，或者页面上按钮的某个配色方案等)。
* 层(layer): 一个应用中，业务方可能会同时进行几组不同的AB对照实验，其中每一组AB对照实验即为一层。其中每一层都会使用到全部的流量。
* 域(domain): 在AB实验过程中，实验者可以配置多个域。在选择实验策略时，会先将流量分配到某个域，再在此域内选择每一层的实验策略。
其中的每个域内选择策略时，只能使用同一种策略选择算法。

目前支持的域的种类有：
  
* 固定组合域（FixedGroup）：此域中的每一层只有一个固定的实验策略。
* 多层正交域（MultiLayerHash）: 使用哈希算法，将流量按照配置的比例划分到每一层的各个实验策略。
  并且保证不同层的实验策略是正交组合。
* Thompson Sampling域(MultiLayerThompson): Thompson Sampling算法实现的策略选择域。
  此域的大部分流量会分配到目前为止表现最好的策略，少部分流量会分配到其它策略进行实验。
* UCB域(MultiLayerUCB): UCB算法实现的策略选择域。此域选择实验策略的规则是，
  如果有新策略则选择新策略，否则选择置信区间上界最大的那个策略。
* EpsilonGreedy域(多层EpsilonGreedy域): 可以指定一定比例的流量做实验（平均分配到各个策略），其它流量全部分配到当前表现最好的策略。

## 配置
* applicationId: 此实验的ID/名称
* totalSize: 总的桶数。流量哈希到域的基数。 如果没有使用Hash策略的域，则可以不设置此参数。
* audienceDefinition: 对实验受众的描述，由一组受众特征
    * features: 受众的特征
        * property: 特征的字段名
        * description：描述
        * keyProperty：boolean类型，是否是key。如果是，则会分配流量的时候用来做hash计算。
    > 如果没有配置受众的描述，则会将传入的object转换为String来计算hash值。
    
* domain: 域的配置
  * domainId: domain的唯一标识
  * ruleStrategy: 流量划分到当前域的策略，取值为[Hash/ExpressionBase]. 可多选。
      * Hash: 会根据当前domain的size和application的totalSize比例，把一定比例的流量分配到当前域
      * ExpressionBase: 会根据ruleExpression把符合规则的受众流量分配到当前域。
      > 在一个实验中，支持两重类型的域同时存在。 流量分配的时候ExpressionBase类型的域会有限匹配，匹配不到的流量才会分配到Hash域。
      如果多个ExpressionBase域的规则有重叠的部分（比如 domain1 的规则是age > 10，domain2的规则是age > 20）流量会分配到第一个匹配到的域。
      另外，ExpressionBase域的size只是本域内的base流量，不受application 的totalSize的限制。 
           
  * ruleExpression: 使用ExpressionBase策略的表达式，例如： (age > 10) and (sex = 男)。 如果有多个限制条件， 每个子条件都需要加小括号。目前支持的表达式有：
      * \> : 大于
      * \< : 小于
      * in : in, 多个条件用逗号隔开。 例如： id in 11,12,13,14
      * = : 等于
      * and : 并且
      * or : 或
      > 如果传入的参数是个基本类型的对象（String, Integer）等， 可以用 _ 表示。 如： _ in 1,2,3,4
  * domainType: domain的类型， 取值为：FixedGroup/MultiLayerHash/MultiLayerUCB/MultiLayerThompson
  * description: domain的描述。
  * size: 当前domain的桶数
  * isDefault: 是否是默认域。 在配置实验策略时，没有指定域ID的策略，都会分配到默认域。
    默认域只能有一个，而且可以不配置size。totalSize - 非默认域的size和就是默认域的size。
* layer：层的配置
  * layerId: 层的唯一标识
  * description: 层的说明信息
  * policies: 本层的所有备选实验策略
    * name : 策略的名称。也是策略的唯一标识。
    * size : 策略所占的桶数。只有分配到MultiLayerHash域，此参数才会起作用。如果是UCB或者Thompson的实验策略，则会根据策略的评估参数进行选择，此项配置不会生效。
    * domainIds : 此策略所属的域。 同一个策略可以指定多个域，也可以不指定域，如果没有指定域，则此策路会分配到默认域。
    * param : 此策略的参数。业务系统自己定义，没有特别的要求。
    * isDefault: 是否是默认实验, 每一层的默认实验最多只能有一个。非必填，默认实验的使用规则如下： 
    >1. 默认实验的domainId配置和size配置都不会生效。默认实验策略会在初始化的时候添加到所有域。
    >2. FixedGroup域如果配置了实验策略，则使用配置的策略，如果没有，则使用默认策略
    >3. MultiLayerHash域的剩余流量（域的总流量 - 所有配置策略的流量）都会分配到默认策略。
    >4. MultiLayerUCB/MultiLayerThompson根据各自的评估算法分配流量
* estimateParam : 实验策略评估参数。如果使用了bandit域（MultiLayerUCB或者MultiLayerThompson）则需要指定此参数。
  每个实验策略对应一个参数。
  * policyName: 策略名称
  * total: 当前策略总的实验次数
  * successes: 当前策略成功转化的次数

## 参考代码

#### 使用Json配置初始化ExperimentAPP

通过如下格式的json，可以直接转换成ExperimentApp.Builder来初始化ExperimentApp。
###### 配置
ab_test.json
```json
{
  "applicationId": "test_hash_domain",
  "totalSize": 1000,
  "domains": [
    {
      "domainId": "multi",
      "domainType": "MultiLayerHash",
      "ruleStrategy": "Hash",
      "description": "",
      "default": true
    },
    {
      "domainId": "fix",
      "domainType": "FixedGroup",
      "ruleStrategy": "ExpressionBase",
      "description": "Old Config No.0",
      "ruleExpression": "_ < 3000"
    }
  ],
  "layers": [
    {
      "layerId": "L1",
      "description": "第一层",
      "policies": [
        {
          "domainIds": ["fix"],
          "name": "E1",
          "param": {
            "test2": 2
          }
        },
        {
          "domainIds":["multi"],
          "name": "E2",
          "size": 300,
          "param": {
            "test3": 3
          }
        },
        {
          "name": "E3",
          "default": true,
          "param": {
            "test3": 4
          }
        },
        {
          "name": "E4",
          "size": 200,
          "param": {
            "test3": 5
          }
        }
      ]
    },
    {
      "layerId": "L2",
      "description": "第二层",
      "policies": [
        {
          "domainIds": ["fix"],
          "name": "F1",
          "param": {
            "test2": 2
          }
        },
        {

          "domainIds": ["multi"],
          "name": "F2",
          "size": 400,
          "param": {}
        },
        {
          "name": "F3",
          "size": -1,
          "default": true,
          "param": {
            "test3": 3
          }
        }
      ]
    }
  ]
}

```
策略评估参数的配置（L1.json）
```json
[
  {
      "policyName": "E1",
      "total": 1000,
      "successes": 600
  },{
    "policyName": "E2",
    "total": 1000,
    "successes" : 600
  },
  {
    "policyName": "E3",
    "total": 1000,
    "successes": 500
  },
  {
    "policyName": "E4",
    "total": 1000,
    "successes": 500
  }
]
```
通过Json配置初始化ExperimentApp
```
        File file = new File(getClass().getClassLoader().getResource("ab_test.json").toURI());
        String configStr = Files.toString(l1File, Charsets.UTF_8);

        ExperimentApp.Builder builder = JSONObject.parseObject(configStr, ExperimentApp.Builder.class);
        File l1File = new File(getClass().getClassLoader().getResource("layers/L1.json").toURI());
        String l1String = Files.toString(l1File, Charsets.UTF_8);;

        builder.setEstimateParam(JSONObject.parseArray(l1String, EstimateParam.class));
        ExperimentApp experimentApp = builder.build();
```

#### 获取实验策略
```
    ABPolicyModel policyModel = app.getPolicy(key);  //policyModel中包含所有层的策略
    Policy policy = policyModel.getPolicy(layerId);
```

#### 更新实验参数
```
    // 参数重新赋值
    app.putEstimateParam(param);
    // 增量更新参数
    app.increaseParam(param);
```

#### 其它配置方式：
除了使用json配置以外，也可以通过API初始化域和层以及策略的配置（比如持久化到表中），创建ExperimentApp方法如下：
###### 配置domain
```
    Domain domain = new Domain();
    domain.setDomainId("OLD");
    domain.setDefault(true);   //非必填，默认false
    domain.setIdList(Lists.newArrayList("1","2","3"));    //非必填
    domain.setSize(100);       // 默认1000
    domain.setDomainType(EnumDomainType.MultiLayerHash);    //必填
    List<Domain> domains = Lists.newArrayList();
    domains.add(domain);
```
###### 配置layer和Policy
```
    Layer l1 = new Layer();
        l1.setLayerId("L1");
        List<Policy> policies = Lists.newArrayList();
        l1.setPolicies(policies);
        Policy policy = new Policy();
        policy.setName("p1");
        policy.setDomainIds(Sets.newHashSet("d1"));  //一个policy可以同属多个domain
        policy.setSize(300);                        //policy属于MultiLayerHash 类型的domain时，需要配置此项
        policies.add(policy);
        ...
```

###### 初始化ExperimentAPP
```
    Map<String, List<EstimateParam>> params = Maps.newHashMap();    // 策略评估参数，key为所属Layer的ID， value为本层所有策略的参数。 
    // 配置各层的参数
    ...
    ExperimentApp.Builder builder = new ExperimentApp.Builder();
    ExperimentApp app = builder.setApplicationId(applicationId)
            .setDomains(domains)
            .setAudienceFeatures(features)
            .setLayers(layers)
            .setTotalSize(size)
            .setEstimateParams(params)      // 只有使用MultiLayerThompson或MultiLayerUCB域时才需要添加此配置
            .build();
```