show tables;

create table board2 (
	idx int not null auto_increment,   	/* 게시글의 고유번호*/
	nickName varchar(20)  not null,     /* 게시글 올린 사람의 닉네임 (회원가입시 닉네임길이랑똑같이줘야함)*/
	title    varchar(100) not null,			/* 게시글의 글 제목*/
	email     varchar(50), 							/* 글쓴이의 메일주소 ,회원가입시엔 필수로했으나 여기선 필순는아님- 사용자가 이메일공개하기 싫을수도있어서 */
	homePage varchar(50),								/* 글쓴이의 홈페이지(블로그)주소*/
	content  text not null,							/* 글 내용 - 글쓰러 온거니까	not null*/
	wDate		 datetime default now(),    /* 글 올린 날짜 -사용자가 입력하는거아님*/
	hostIp   varchar(50)	not null,     /* 글 올린이의 접속 IP*/
	readNum	 int default 0,     				/* 글 조회수*/
	good		 int default 0,							/* '좋아요' 클릭 횟수 누적하기 - 좋아요를 여러군데 테이블에 쓰려면 테이블은 따로빼는게좋음*/
	/*댓글은 여기에서 설계안함 -테이블 따로빼기 ( 몇개들어올지도모르고,댓글달지안달지도 몰라서) */
	mid			 varchar(20) not null,			/* 회원아이디 ( 내가 올린 게시글 전체 조회시 사용-닉네임은 바꿀수있게 이미설계를해놔서 아이디는 못바꾸는거니까 조회하고싶을때 아이디로조회) */
	primary key(idx)
);
/* 필수입력은 전에만든(회원가입)길이 최대한 같게하는게 좋음*/

desc board2;

insert into board2 values (default, '관리맨','게시판서비스를 시작합니다','asdf1234@naver.com','http://hkd1234.tistory.com','이곳은 게시판입니다',default,'192.168.50.210',default,default,'admin');

select * from board2;

/* 게시판에 댓글 달기 테이블 */
create table boardReply2 (
	idx int not null auto_increment,  /* 댓글의 고유번호 */
	boardIdx int not null, 						/* 원본글의 고유번호(외래키로 지정)-사용자가 입력x*/
	mid      varchar(20) not null,    /* 댓글 올린이의 아이디 (나중에 닉네임바꾸고 조회하고싶을때사용)*/
	nickName varchar(20) not null,    /* 댓글 올린이의 닉네임*/
	wDate    datetime default now(),  /* 댓글 올린날짜*/
	hostIp   varchar(50) not null,    /* 댓글 올린 PC의 IP*/
	content  text not null,   				/* 댓글 내용*/
	level 	 int not null default 0,  /* 댓글레벨 - 첫번째댓글(부모댓글)의 레벨은 0번 */
	levelOrder int not null default 0,  /* 댓글의 순서 -첫번째 댓글(부모댓글)의 levelOrder는 0번*/
	primary key(idx),
	foreign key(boardIdx) references board2(idx)  /*외래키 -board2의 idx를 참조함*/
	/*on update cascade   유니코드키- 외래키와 주키가 둘다 숫자만 아주잘먹음, 외래키가 문자면 에러가남, 이걸해야 나중에 ER다이어그램을함*/
	/*on delete restrict */
);

desc boardReply2;


/* 날짜함수 처리 연습 */
select now();           -- now() : 오늘'날짜'와 '시간'을 보여줌
select year(now());     -- year() : '년도' 출력
select month(now());    -- month() : '월' 출력
select day(now());      -- day() : '일' 출력
select date(now());     -- date() : '년-월-일' 출력
select weekday(now());  -- 요일 : 0(월), 1(화), 2(수),3(목),4(금),5(토),6(일)
select dayofweek(now());-- 요일 : 1(일), 2(월),3(화),4(수),5(목),6(금),7(토)
select hour(now());     -- hour() : 시간(24h)
select minute(now());   -- minute() : 분
select second(now());   -- second() : 초

select year('2022-12-1');  				 						-- 이렇게 직접줄수도있음-년도만출력
select idx,year(wDate) from board2; 						-- board2테이블의 idx와 년도만출력
select idx, day(wDate) as 날짜 from board2; 		-- 일 출력, wDate대신에 별명인 날짜로 나옴
select idx, wDate, weekday(wDate) from board2; -- idx , 날짜 , 요일

/* 날짜 연산*/
-- date_add(date, interval 값 type)
select date_add(now(), interval 1 day);   --       오늘날짜보다 +1일 출력*/
select date_add(now(), interval -1 day);  --       오늘날짜보다 -1일 출력
select date_add(now(), interval 10 day_hour);  --  오늘날짜보다 +10시간 출력
select date_add(now(), interval -10 day_hour);  -- 오늘날짜보다 -10시간 출력


--date_sub(date, interval 값 type) --반대로
select date_sub(now() , interval 1 day);  -- 오늘날짜보다 -1일 출력
select date_sub(now() , interval -1 day); -- 오늘날짜보다 +1일 출력

select idx, wDate from board2;
-- date_format(날짜,'양식기호')
-- 년도(2자리) : %y, 년도(4자리) : %Y, 월: %m, 월(영문):%M, 일:%d , 시(12시간제):%h, 시(24시간제):%H, 분:%i,초: %s
select idx, date_format(wDate,'%y-%M-%d')from board2; -- %M 월을 영문출력 (ex.22-November-28)
select idx, date_format(wDate,'%y-%m-%d')from board2; -- %m 월을 숫자출력 (ex.22-11-28)
select idx, date_format(wDate,'%Y-%m-%d')from board2; -- %Y 월을 4자리출력 (ex.2022-11-28)
select idx, date_format(wDate,'%h-%i-%s')from board2; --  시-분-초 출력 (ex.11-17-59)


--현재부터 한달전의 날짜 
select date_add(now(),interval -1 month);

-- 하루전 체크
select date_add(now(), interval -1 day);
select date_add(now(), interval -1 day), wDate from board2; -- board2테이블의 하루전 체크


-- 날짜차이 계산 : DATEDIFF(시작날짜, 마지막날짜)  => (시작날짜 - 마지막날짜)계산결과나옴
select datediff('2022-11-30' , '2022-12-01');  				-- -1나옴
select datediff(now(),'2022-11-30'); 									-- 2022-12-01기준 1나옴
select idx, wDate, datediff(now() ,wDate) from board2;	-- 오늘날짜 - wDate날짜
/* 이 위에까지는 연습 아래는 변수로 받아내서 얘랑 원하는애랑 비교해서 */
select idx, wDate, datediff(now() ,wDate) as day_diff from board2; -- datediff를 day_diff변수로 별명을줌
select *, datediff(now() ,wDate) as day_diff from board2; -- 필드 모두나오고 day_diff필드를 하나더 추가해서 보여줌 (vo 에넣기)

-- 시간차이 계산: TIMESTAMPDIFF(단위, 날짜1, 날짜2);   (날짜2 - 날짜 1)
select timestampdiff(hour, now(), '2022-11-30'); -- 현재 날짜와 2022-11-30 간의 시간(hour)차이
select timestampdiff(hour,'2022-11-30',now());
select timestampdiff(hour, wDate ,now()) from board2; -- 현재날짜와 wDate 간의 시간차이
select *,timestampdiff(hour, wDate ,now()) as hour_diff from board2;  -- 필드 모두나오고 시간차이를 hour_diff별명으로하고 이 필드를 하나더 추가해서 보여줌
select *,datediff(now() ,wDate) as day_diff ,timestampdiff(hour, wDate ,now()) as hour_diff from board2; -- 날짜차이계산과 시간차이계산을 각각 별명으로 필드를추가해서 보여줌


/* 이전글/다음글 체크 */
select * from board2 where idx < 5; -- idx가 5보다 작은것들을 보여달라
select * from board2 where idx < 5 order by idx desc limit 1; -- idx가 5보다 작은걸 1개만 내림차순으로 보여달라 (이전글체크)
select * from board2 where idx > 5 limit 1; -- idx가 5보다 큰걸 1개만 보여달라 (다음글)


/* 댓글의 수를 전체 List에 출력하기 연습 */
select * from board2Reply order by idx desc; /*댓글테이블의 내용을 내림차순으로 출력*/

-- 댓글테이블(board2Reply)에서 원본글테이블(board2)의 고유번호 31번글에 달려있는 댓글의 개수는 ?
select count(*) from boardReply2 where boardIdx= 31; -- 원본글 31번에 달려있는 댓글 한건만출력

-- 댓글테이블(boardReply)에서 원본글테이블(board)의 고유번호 31번글에 달려있는 댓글의 개수는 ?
-- 원본글의 고유번호와 함께 출력, 갯수의 별명은 replyCnt
select boardIdx,count(*) as replyCnt from boardReply where boardIdx = 31;

-- 댓글테이블(boardReply)에서 원본글테이블(board)의 고유번호 31번글에 달려있는 댓글의 개수는 ?
-- 원본글의 고유번호와 함께 출력, 갯수의 별명은 replyCnt
-- 이때 원본글을 쓴 닉네임을 함께 출력하시오. 단 닉네임은 board(원본글)테이블에서 가져와서 출력하시오
SELECT boardIdx,
 (SELECT nickName FROM	board2 where idx = 31) AS nickName,
 count(*) AS replyCnt
 FROM boardReply2 WHERE boardIdx = 31; /*서브테이블*/

-- 앞의 문장을 부모테이블(board)의 관점에서 보자...
SELECT mid,nickName FROM board where idx = 31;	

-- 앞의 닉네임을 자식(댓글) 테이블(boardReply)에서 가져와서 보여준다면 ???
SELECT mid,
	(select nickName from boardReply2 where boardIdx=31) as nickName
	FROM board2 where idx = 31; /* 서브쿼리가 하나만 있지않아서 에러남 */
	
-- 부모관점(board2)에서 고유번호 31번의 아이디와, 현재글에 달려있는 댓글의 개수?
SELECT mid,
	(SELECT count(*) FROM boardReply2 WHERE boardIdx=31) 
	FROM board2 WHERE idx=31;
	

-- 부모관점(board2)에서 board2테이블의 모든 내용과(댓글), 현재글에 달려있는 댓글의 개수를 가져오되, 최근글 5개만출력?
SELECT *,
	(SELECT count(*) FROM boardReply2 WHERE boardIdx=board.idx) as replyCount
	FROM board2
	order by idx desc
	limit 5;

-- 부모관점(board2)에서 board2테이블의 모든 내용과(댓글), 현재글에 달려있는 댓글의 개수를 가져오되, 최근글 5개만출력?
-- 각각의 테이블에 별명을 붙여서 앞의 내용을 변경시켜보자.
SELECT *,
	(SELECT count(*) FROM boardReply2 WHERE boardIdx=b.idx) as replyCount
	FROM board2 b
	order by idx desc
	limit 5;
/*b라고 별명을 줌*/
