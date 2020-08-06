# 🔙 백엔드 미션

레벨2 동안 우리는 웹 프로그래밍을 ATDD 기반으로 개발하는 경험을 했습니다.  
기능을 처음부터 구현해보기도 했고 레거시가 있는 상황에서 기능을 추가해보기도 했었죠.  
과정을 잘 소화했는지 점검해보고 스스로를 되돌아보기 위해 `레거시 지하철 노선도 애플리케이션 기능 추가` 미션을 준비했습니다.    
아래의 요구사항을 만족하는 기능을 구현해주세요.

<br/>

## 🎯 요구사항

- [ ]  1. 경로 조회 응답 결과에 `요금` 정보 추가(필수)
- [ ]  2. `가장 빠른 경로 도착` 경로 타입 추가(선택)

<br/>

## 💻 프로그래밍 요구사항
- [x]  1. `인수 테스트`를 작성하세요.
- [ ]  2. 경로 조회 기능의 `문서화`를 진행하세요.
- [ ]  3. 기능 구현 시 `TDD`를 활용하여 개발하세요. (커밋 단위를 TDD 사이클을 확인할 수 있으면 좋겠네요!)

<br/>

## 📑 기능 요구사항 상세 설명

### 1. 경로 조회 응답 결과에 `요금` 정보 추가

현재 `경로 조회` 기능([PathAcceptanceTest](https://github.com/woowacourse/atdd-subway-2020/blob/master/src/test/java/wooteco/subway/maps/map/acceptance/PathAcceptanceTest.java) )이 구현되어 있습니다.    
[요금 계산 기준](#요금-계산-기준)을 참고하여 기존 응답에 `요금` 정보도 함께 응답이 될 수 있도록 기능을 추가로 구현하세요.  
아래와 같이 `요금` 정보가 포함되어야 합니다. 
 
```json
{
    "stations": [
        {
            "id": 1,
            "name": "교대역"
        },
        {
            "id": 2,
            "name": "강남역"
        },
        {
            "id": 3,
            "name": "양재역"
        }
    ],
    "duration": 3,
    "distance": 4,
    "fare": 1250
}
```

#### 요금 계산 기준
```
## 거리별 요금 
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
  - 10km초과 ∼ 50km까지(5km마다 100원)
  - 50km초과 시 (8km마다 100원)

## 노선별 추가 요금
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
  - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- 가장 높은 추가 요금만 적용
  - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

## 연령별 할인 정책
- 로그인 사용자의 경우 연령별 요금으로 계산
  - 청소년(13세 이상~19세 미만): 운임에서 350원을 공제한 금액의 20%할인
  - 어린이(6세 이상~ 13세 미만): 운임에서 350원을 공제한 금액의 50%할
```

<br/>

### 2. `가장 빠른 도착` 경로 타입 추가

현재 `경로 조회` 기능은 `최단 거리`와 `최소 시간` 기준으로 조회가 가능합니다.  
경로 조회의 새로운 기준인 특정 시간 기준 `가장 빠른 도착` 조회 기능을 추가하세요.  
목적지에 도착하는 시간을 계산하기 위해서는 경로의 각 지하철역에 도착하는 시간을 고려해야 합니다.  
도착하는 시간 이후 가장 빠른 출발 시간을 조회하는 반복 로직이 포함되어야 합니다.

<br/>

#### 기능 제약사항
- 노선의 첫차/막차 시간은 24:00을 넘기지 못하며 첫차 시간은 막차 시간 보다 항상 이르다.
- 막차가 끊길 경우 다음날 첫차 기준으로 계산한다.
- 이동 시간과 승하차 시간은 고려하지 않는다. 1호선과 2호선이 교차하는 C역에서 1호선에서 2호선으로 환승해야 하는 경우 1호선 도착 시간이 14:30이고 2호선 출발 시간이 14:30일 경우에도 환승할 수 있음
  
<br/>

#### `가장 빠른 도착` 경로 계산 방법

ex) 14:00에 A역에서 D역으로 이동할 때 A-B-D와 A-C-D 경로가 존재하는 경우

- A-B 구간의 노선을 조회하고 상행/하행을 판단
- 노선의 첫차 시간과 간격을 활용하여 14:00 이후 A-B 방향으로 A역에 가장 빨리 도착하는 시간 계산
- A-B의 소요시간을 조회하여 B역 도착 시간을 계산
- B역 도착 시간 이후 B-D 구간의 노선을 조회하고 상행/하행을 판단
- 노선의 첫차 시간과 간격을 활용하여 B역 도착 시간 이후 B-D 방향으로 B역에 가장 빨리 도착하는 시간 계산
- B-D의 소요시간을 조회하여 D역 도착 시간을 계산
- 같은 방법으로 A-C-D 경로의 도착 시간을 계산하여 빨리 도착하는 경로를 응답

<br/>

#### 기존 코드 설명 - Line(노선)
- 노선은 시간과 관련된 첫차 시간, 막차 시간, 간격의 정보를 가지고 있음
- 각 노선의 첫 역에서 마지막 역으로 가는 방향(하행)과 마지막 역에서 첫 역으로 가는 방향(상행)이 존재함
- 첫차 시간은 첫 역과 마지막 역에서 하루 중 처음 지하철이 출발하는 시간을 의미
- 막차 시간은 첫 역과 마지막 역에서 하루 중 마지막으로 지하철이 출발하는 시간을 의미

<br/>

## 힌트
### `가장 빠른 도착` 경로 조회 시

출발역과 도착역의 모든 경로를 조회한 후 각 경로의 도착 시간을 계산하여 가장 빠른 시간의 경로를 찾아야 합니다.  
이 때, 모든 경로 조회는 KShortestPath를 활용할 수 있으며 Jgrapht의 라이브러리를 활용할 수 있습니다.  
Jgrapht의 KShortestPaths 활용 방법은 [JgraphTest](https://github.com/woowacourse/atdd-subway-2020/blob/master/src/test/java/wooteco/study/jgraph/JgraphTest.java#L35) 를 참고 하세요.

<br/>

### 미션 수행 사이클(ATDD)

![](https://nextstep-storage.s3.ap-northeast-2.amazonaws.com/2020-07-03T11%3A31%3A48.874image.png)

요구사항을 파악 후 인수 테스트 작성을 시작으로 미션을 수행해주세요.  
인수 테스트 작성 후 문서화를 진행하시고 이후 TDD를 통해 기능 구현을 해주세요.  

<br/>

### 로그인
[AuthAcceptanceTest](https://github.com/woowacourse/atdd-subway-2020/blob/master/src/test/java/wooteco/subway/members/member/acceptance/AuthAcceptanceTest.java) 의 인수 테스트와 기존 기능을 참고하여 로그인 기능을 파악하세요.

<br/>

### 5km 마다 100원 추가
```java
private int calculateOverFare(int distance) {
    if (distance == 0) {
        return 0;
    }
    return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
}
```

<br/>

### JPA 엔티티 필드 추가

노선별 추가 요금 기능을 구현하기 위해 Line 클래스에 `extraFare` 필드를 추가해야 합니다.  
```java
@Entity
public class Line extends BaseEntity {

    ...
    
    private LocalTime endTime;
    private int intervalTime;
    private int extraFare;
    
    @Embedded
    private LineStations lineStations = new LineStations();

    ...
}
```

물론 `@Embedded`을 활용하여 구현해도 좋습니다.

```java
@Entity
public class Line extends BaseEntity {

    ...
    
    private LocalTime endTime;
    private int intervalTime;
    
    @Embedded
    private Fare extraFare;
    
    @Embedded
    private LineStations lineStations = new LineStations();

    ...
}
```