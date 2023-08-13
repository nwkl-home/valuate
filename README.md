# valuate

基于[govaluate](https://github.com/Knetic/govaluate)提供对java版本规则引擎的支持。


### 设计有4种类型概念，基于以下类型进行计算:
+ 字符串(String)
+ 布尔(Boolean)
+ 数值(BigDecimal)，对应byte, short, char int, float, long, double, BigDecimal等
+ 其他(即非以上三种)，比如自定义各种类，数组等

### 支持以下规则
+ 支持基本的逻辑表达式和算数表达式： + - * / % & | ^ >> << ** && ||，其中 - 可以是减也可以是取负，**是幂次方
+ 支持比较判断：== != > >= < <= =~ !~ in，其中 =~ !~ 是字符串正则表达式判断，in是判断是否存在某个数组或者列表中
+ 支持三元表达式： 格式isXXX ? true: false
+ 支持函数调用， 比如add(1, 2) => 1 + 2 = 3， add需要自定义
+ 支持对象访问器，或者map访问器，比如user = User{name: 'lwkl'}, 表达式"user.name" => 'lwkl'

### 例子
+ 基本逻辑表达式或算数表达式
```java
public class Main {

    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("a", 2);
        EvaluableExpression e1 = new EvaluableExpression("a + 1", functionMap); // 3 使用变量
        EvaluableExpression e2 = new EvaluableExpression("1 + 1", functionMap); // 2 +
        EvaluableExpression e3 = new EvaluableExpression("2 - 1", functionMap); // 1 -
        EvaluableExpression e4 = new EvaluableExpression("3 * 2", functionMap); // 6 *
        EvaluableExpression e5 = new EvaluableExpression("3 >> 1", functionMap); // 1 >>
        EvaluableExpression e6 = new EvaluableExpression("2 << 3", functionMap); // 16 <<
        EvaluableExpression e7 = new EvaluableExpression("true && false", functionMap); // false &&
        EvaluableExpression e8 = new EvaluableExpression("false || true", functionMap); // true ||
        EvaluableExpression e9 = new EvaluableExpression("3 % 2", functionMap);  //1 %
        EvaluableExpression e10 = new EvaluableExpression("3 ** 2", functionMap); //9
        System.out.println(e1.evaluate(parameters));
        System.out.println(e2.evaluate(parameters));
        System.out.println(e3.evaluate(parameters));
        System.out.println(e4.evaluate(parameters));
        System.out.println(e5.evaluate(parameters));
        System.out.println(e6.evaluate(parameters));
        System.out.println(e7.evaluate(parameters));
        System.out.println(e8.evaluate(parameters));
        System.out.println(e9.evaluate(parameters));
        System.out.println(e10.evaluate(parameters));

    }
}
```
+ 比较判断
```java
public class Main {

    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("a", 2);
        List<Object> list = new ArrayList<Object>();
        list.add("1");
        list.add(2);
        parameters.put("list", list);
        EvaluableExpression e1 = new EvaluableExpression("a == 2", functionMap); // true
        EvaluableExpression e2 = new EvaluableExpression("a != 2", functionMap); // false
        EvaluableExpression e3 = new EvaluableExpression("a >= 2", functionMap); // true
        EvaluableExpression e4 = new EvaluableExpression("a > 2", functionMap); // false
        EvaluableExpression e5 = new EvaluableExpression("a <= 2", functionMap); // true
        EvaluableExpression e6 = new EvaluableExpression("a < 2", functionMap); // false
        EvaluableExpression e7 = new EvaluableExpression("'abc' =~ 'a'", functionMap); // true
        EvaluableExpression e8 = new EvaluableExpression("'abc' !~ 'a'", functionMap); // false
        EvaluableExpression e9 = new EvaluableExpression("a in list", functionMap);  // true
        EvaluableExpression e10 = new EvaluableExpression("1 in list", functionMap); // false
        System.out.println(e1.evaluate(parameters));
        System.out.println(e2.evaluate(parameters));
        System.out.println(e3.evaluate(parameters));
        System.out.println(e4.evaluate(parameters));
        System.out.println(e5.evaluate(parameters));
        System.out.println(e6.evaluate(parameters));
        System.out.println(e7.evaluate(parameters));
        System.out.println(e8.evaluate(parameters));
        System.out.println(e9.evaluate(parameters));
        System.out.println(e10.evaluate(parameters));

    }
}
```
+ 三元表达式
```java
public class Main {

    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("a", 2);
        List<Object> list = new ArrayList<Object>();
        list.add("1");
        list.add(2);
        parameters.put("list", list);
        EvaluableExpression e1 = new EvaluableExpression("a == 2 ? 'a': 'b'", functionMap); // a
        EvaluableExpression e2 = new EvaluableExpression("a != 2 ? 'a': 'b'", functionMap); // b
        System.out.println(e1.evaluate(parameters));
        System.out.println(e2.evaluate(parameters));
    }
}
```
+ 自定义函数调用

实现Function接口，建议使用判断参数个数、类型，增强程序健壮性
```java
public class Main {

    // 无参数
    public static final Function fn = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return 1;
        }
    };

    // 有参数
    public static final Function fn1 = new Function() {
        public Object execute(Object... parameters) throws Exception {
            return parameters[0];
        }
    };

    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("a", 2);
        Map<String, Function> functionMap = new HashMap<String, Function>();
        functionMap.put("fn", fn);
        EvaluableExpression e1 = new EvaluableExpression("fn() + 2", functionMap); // 3
        EvaluableExpression e2 = new EvaluableExpression("fn1(3) + 2", functionMap); // 5
        System.out.println(e1.evaluate(parameters));
        System.out.println(e2.evaluate(parameters));
    }
}
```
+ 对象访问器、map访问器
```java
public class Main {

    static class User {
        private String userName;

        public User(String userName) {
            this.userName = userName;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String get(String userId, String userName) {
            return userId + "-" + userName;
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<String, Object>();
        Map<String, Object> a = new HashMap<String, Object>();
        a.put("b", 1);
        parameters.put("user1", new User("lwkl"));
        parameters.put("a", a);
        Map<String, Function> functionMap = new HashMap<String, Function>();
        EvaluableExpression e1 = new EvaluableExpression("user1.userName", functionMap); // lwkl
        EvaluableExpression e2 = new EvaluableExpression("user1.getUserName()", functionMap); // lwkl
        EvaluableExpression e3 = new EvaluableExpression("user1.get('1', 'lwkl')", functionMap); // 1-lwkl
        EvaluableExpression e4 = new EvaluableExpression("a.b", functionMap); // 1
        System.out.println(e1.evaluate(parameters));
        System.out.println(e2.evaluate(parameters));
        System.out.println(e3.evaluate(parameters));
        System.out.println(e4.evaluate(parameters));
    }
}
```

### 后期改进项
+ 区分抛出异常定义，目前全部都是Exception
+ 增加函数配置注解，更加便捷配置
+ 压力测试，优化性能

联系本人
--

欢迎探讨：+V nuanwei1314 




