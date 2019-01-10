# GetPOIFromAmap
基于高德开放服务之web API提供的搜索POI接口爬取数据

## 说明
多边形搜索API服务地址：https://restapi.amap.com/v3/place/polygon?parameters
parameters代表的参数包括必填参数和可选参数。所有参数均使用和号字符(&)进行分隔。下面的列表枚举了其中一些参数及其使用规则：

polygon: 
必填
规则：经度和纬度用","分割，经度在前，纬度在后，坐标对用"|"分割。经纬度小数点后不得超过6位。        
多边形为矩形时，可传入左上右下两顶点坐标对；其他情况下首尾坐标对需相同。

type：
可选
分类代码由六位数字组成，一共分为三个部分，前两个数字代表大类；中间两个数字代表中类；最后两个数字代表小类。
若指定了某个大类，则所属的中类、小类都会被显示。
当指定010000，则010100等中类、010101等小类都会被包含。
当指定010900，则010901等小类都会被包含
分类代码下载地址：https://lbs.amap.com/api/webservice/download
注：当keywords和types为空的时候， 我们会默认指定types为120000（商务住宅）&150000（交通设施服务） 

offset
可选
每页记录数据
强烈建议不超过25，若超过25可能造成访问报错
默认20

page
可选
当前页数
最大翻页数100

## 使用

首先 我们需要设置我们数据限额，一个小于1000的数，我们设置为800吧。

```java
	static final int NUM = 800;//可以范围的最大数目，如果大于800，多出的数目不会返回
```

然后 我们定义一个我们的索引范围：研究范围
可以根据左上和右下两个点确定矩形范围
```java
//通过左上角和右下角两点确定矩形范围，进行多边形搜索
	//输入坐标范围为WGS-84坐标系
	static double lux = 120.852722;//左上X
	static double luy = 31.874695;//左上Y
	static double rdx = 122.242493;//右下X
	static double rdy = 30.677917;//右下Y
```
接下来 我们设置我们的KEY、要获取的POI类型码、保存的txt的路径

```java
	static String[] type = {"090100","090101",
			"090102","0902","0903","0904",
			"0905","0906","0907"};//type和keyword必须二选一输入参数，这里选择type
```

```java
	static String citycode = "021";//城市编码
	static String path = "C:/个人/data/poi/";	
	static int offset = 20;//每页显示的POI数目，限制小于25
```
在接口之中，可以通过city&citylimit参数指定希望搜索的城市或区县。而city参数能够接收citycode和adcode，citycode仅能精确到城市，而adcode却能够精确到区县。这里选择citycode，如果研究范围是一个市的话。

### 返回属性
包括经纬度、名称、类别代码、城市代码等
```java
public class Poi {
	
	public String id,name;
	public String location; //经纬度坐标
//	public String address; //地址 若是地铁站，则为地铁线路名称
	public String citycode, cityname;
	public String adcode, adname;
	public String typecode, type;
}
```
例如：
> {"id":"B00156CXQZ","name":"沃尔玛免费班车(上客站)","location":"121.512053,31.302162","address":"国宾路与政通路交叉口东南50米","citycode":"021","cityname":"上海市","adcode":"310110","adname":"杨浦区","typecode":"150800","type":"交通设施服务;班车站;班车站"}

