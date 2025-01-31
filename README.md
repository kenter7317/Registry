# Registry
Registry는 value 값을 지정해주면 Yaml에서 읽어와서 실행시간에 올리는 라이브러리가 될 예정 이였습니다!
그러나 클래스 로딩 문제로 망가진 라이브러리가 되어 버렸죠!
고쳐서 큰 기여 해주실 분을 위해서 Example 남겨둡니다

```java
@Registry(value = "abc")
private String abc;

public static void main(){
  System.out.println(abc)
}
```
registry.yaml
```yml
abc: "logical"
```
output
```cmd
logicial
```

