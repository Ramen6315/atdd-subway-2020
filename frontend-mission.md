# 🤹‍♂️ 프론트엔드 미션

현재 지하철 앱의 프론트엔드는 Vuejs 기반입니다.  
그런데 프론트엔드 개발자가 급체를 하여 여러분이 빠르게 api를 추가하고, 기능을 구현해야 합니다. 😱  
다행히 프론트엔드 개발자에게 문의해본 결과, Vuejs를 알지 못하더라도 충분히 구현할 수 있다고 합니다. 🤔

<br/>

## 🚀 Getting Started

```
cd frontend
npm install
npm start
```

- frontend 디렉토리에서 수행해야 합니다.
- [localhost:8081](http://localhost:8081) 로 접근 가능합니다.

<br/>

## 🎯 요구사항

- [x]  1. 백엔드 요금 조회 api를 프론트엔드에서 사용할 수 있게 연동
- [ ]  2. [템플릿 리터럴](https://developer.mozilla.org/ko/docs/Web/JavaScript/Reference/Template_literals)을 이용해 현재 시간을 사용자가 보기 편한 형식으로 문자열 렌더링
- [x]  3. validator를 구현해, form의 유효성을 검사
- [ ]  4. 길찾기를 위해 사용자가 입력한 값을 이용해 검색결과를 불러오는 핸들러를 구현

<br/>

## 📑 기능 요구사항 상세 설명

### 1. 요금 조회 api를 프론트엔드에서 사용할 수 있게 연동

백엔드에서 추가로 구현하는 요금조회의 api를 직접 프론트엔드에 연동해야 합니다.

- [x]  `frontend/src/api/modules/path.js` 에 백엔드에서 구현한 path 검색 api의 endpoint를 추가합니다.

```js
const PathService = {
  get() {
    return ApiService.get(``)
  }
}
```

- 위 apiService를 사용하는 곳은 `frontend/src/store/modules/path.js` 입니다.
- [x]  여러분이 api를 추가하고, 함께 업데이트 할 부분은 `actions`의 `PathService.get()` 부분의 파라미터입니다. 

```js
const actions = {
  async searchPath({ commit }, {}) {
    return PathService.get().then(({ data }) => {
      commit('setPath', data)
    })
  }
}
```

- api 요청이 성공하면, 프론트엔드에서 자동으로 변경된 값을 확인하고, 렌더링이 반영됩니다.
#### 참고
- `frontend/src/store/modules/path.js` 에 구현된 객체들의 역할은 아래와 같습니다.
  - actions - api 비동기 요청
  - mutations - 전역으로 관리할 상태 데이터 변경
  - getters - 컴포넌트들에서 특정 상태값을 조회하고 싶을 때 사용하는 getter

<br/>
<br/>

### 2. validator 구현

- [ ]  path와 departureTime form의 validation을 구현하기
- 공통적으로는 빈 값이 없는지 확인하는 로직이 필요합니다.
- path form에서 필요한 유효성 검사
    - [ ] source: 유효한 Id 값인지 검사 (예를 들면, 양의 정수, 자연수 등)
    - [ ] target: 유효한 Id 값인지 검사 (예를 들면, 양의 정수, 자연수 등)
- departureTime form에서 필요한 검사
    - [ ] dayTime: '오전' or '오후'인지 검사
    - [ ] hour: 숫자 타입, 1~12 사이의 정수인지 검사
    - [ ] minute: 숫자 타입, 0~60 사이의 정수인지 검사
    
<img width="600" src="https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/9290f4cf0aef4b2e92d342741085c3ba">

- 현재 다른 페이지 form들의 validation이  `frontend/src/utils/validator.js` 구현되어있습니다.
- path 검색을 위한 form과, departureTime form의 validation을 추가해야 합니다.
- validation은 Array에 파라미터로 받은 value를 check할 함수를 아래와 같이 추가하면 됩니다.
```js
stationName: [(v) => !!v || '이름 입력이 필요합니다.', (v) => v.length > 0 || '이름은 1글자 이상 입력해야 합니다.'],
```
- `path`, `departureTime` form 모두 validator만 구현하면 자동으로 적용되게끔 설정되어있습니다. 따라서 validator만 구현해주시면 됩니다.

```js
const validator = {
  path: {
    source: [],
    target: []
  },
  departureTime: {
    dayTime: [],
    hour: [],
    minute: []
  }
}
```


<br/>
<br/>

### 3. 현재 시간을 보여주는 `getCurrentTime` 메서드 구현

- [x]  현재시간을 아래 format처럼 보이도록,  `frontend/src/views/path/pathPage.vue` 의 208line에 있는 `getCurrentTime`을 구현합니다.

<img width="400" src="https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/e656d0d51639446f93cd3af7222492dd">

- 자바스크립트에서 현재 시간을 구하는 방법을 찾아보세요.
- 사용예시

```js
getCurrentTime() {
   //현재 시간을 가져와, 아래와 같은 형태의 문자열로 만들 수 있도록 해주세요.
  return '오후 17:08'
}
```

<br/>
<br/>

### 4. 검색결과를 불러오는 `onSearchResult` 메서드 구현

- [ ]  요금조회를 위해 검색 버튼을 눌렀을 때 실행되는 이벤트 핸들러를 구현합니다.
- `frontend/src/views/path/PathPage.vue`에서 `onSearchResult()`를 구현합니다.
- `onSearchResult()` 는 사용자 입력값을 받은 값을 이용해 서버에 요청하고, response를 받아오는 메서드입니다.
- try ~ catch 문에서 try 부분을 구현합니다.
- api 요청하는 메서드는 `this.searchPath()` 입니다.
- 사용자가 입력한 출발역, 도착역의 값은 자동으로 `this.path` 에 저장됩니다.

```js
// this.path는 아래와 같이 구성되어 있습니다.
path: {
  source: sourceId,
  target: targetId,
  type: PATH_TYPE
}

// this.PATH_TYPE은 아래와 같이 구성되어 있습니다.
PATH_TYPE: {
  DISTANCE: 'DISTANCE',
  DURATION: 'DURATION',
  ARRIVAL_TIME: 'ARRIVAL_TIME'
}
```

- 사용자의 입력을 받은 시간에 대한 값은 `this.departureTimeView` 에 저장됩니다.

<img width="400" src="https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/c1980bd471f149ee8573d391a2d7ac6b">

- 빠른 도착시간을 구하는 경우 `YYYYMMDDHHmm` 형태로 time을 파라미터로 보내주세요.
- 사용예시

```js
const hour = this.departureTimeView.hour
const minute = this.departureTimeView.minute
const time = YYYYMMDDHHmm

//최단 거리 또는 최소 시간을 구하는 경우
this.searchPath({ ...this.path })

// 빠른 도착을 구하는 경우
this.searchPath({ ...this.path, time })
```
#### 참고 
- `...`으로 작성하는 기법은 [Spread Operator](https://joshua1988.github.io/es6-online-book/spread-operator.html)라는 기법입니다.
