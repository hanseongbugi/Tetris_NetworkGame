# Tetris_NetworkGame
* Java의 Socket 라이브러리를 이용한 네트워크 테트리스 게임

# 프로젝트 개요
* 일반적인 테트리스 종류의 게임은 친구나 가족끼리 함께할때 경쟁적인 요소가 적다는 문제가 있다.
* 이러한 문제점을 해결하고자 Unqiue에서는 일반적인 테트리스의 규칙에 경쟁적인 요소를 추가한 게임을 제작하고자한다.

# 프로젝트 기능
1. 테트리스 기능
   * 7가지의 렌덤한 블록을 제공하고, 블록이 가로로 1줄이상을 채웠다면 채운 블록을 제거한다
2. 네트워크 기능
   * 네트워크 프로그래밍 기법을 활용하여 2명의 사용자가 서로 통신할 수 있다.
3. 경쟁 기능
   * 한명의 사용자가 블록을 제거한다면 다른 사용자에게 플레이를 방해할 수 있는 요소를 제공한다.

# 실행 순서
1. GameServer Project의 WaitingRoom 패키지 속 GameServer.java를 실행
2. TetrisGame Project의 TetrisGame 패키지 속 GameMain.java를 실행

# 시스템 흐름도
<img width="792" alt="image" src="https://github.com/hanseongbugi/Tetris_NetworkGame/assets/105718365/d4b00648-22aa-42cd-99be-130bd0841eae">

# 프로토콜 목록
![그림1](https://github.com/hanseongbugi/Tetris_NetworkGame/assets/105718365/23ef5ccf-2ad2-44f5-a36f-7319141ec35e)

![그림2](https://github.com/hanseongbugi/Tetris_NetworkGame/assets/105718365/b083f9b9-8d66-45d1-8ad6-54c90623e01e)

# 실행 영상
https://github.com/hanseongbugi/Tetris_NetworkGame/assets/105718365/86eef938-c1fd-4c23-96e7-1dc554f396fc

