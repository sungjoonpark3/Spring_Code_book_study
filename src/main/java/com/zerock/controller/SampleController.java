package com.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerock.domain.SampleVO;
import com.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/sample")
@Log4j
public class SampleController {

	@GetMapping(value="/getText",produces="text/plain; charset=UTF-8")
	public String getText() {
		
		log.info("MIME TYPE:"+MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";
	}
	
	//XML과 JSON으로 데이터를 호출 할 수 있도록 설정 ->MediaType.ApplicationJSON등등
	@GetMapping(value="/getSample",produces= {MediaType.APPLICATION_JSON_UTF8_VALUE,MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		return new SampleVO(112,"스타","로드");
	}
	
	@GetMapping(value="/getSample2")
	public SampleVO getSample2() {
		return new SampleVO(113,"로켓","라쿤");
	}
	
	//컬렉션 타입의 객체반환
	@GetMapping(value="/getList")
	public List<SampleVO> getList(){
		return IntStream.range(1,10).mapToObj(i->new SampleVO(i,i+"First",i+"Last")).collect(Collectors.toList());
	}
	
	//맵의 경우 키와 값을 가지는 하나의 객체로간주
	@GetMapping(value="/getMap")
	public Map<String,SampleVO> getMap(){
		
		Map<String, SampleVO> map = new HashMap<>();
		
		map.put("First", new SampleVO(1111,"그루트","주이너"));
		
		return map;
		
	}
	
	//ResponseEntity 타입
	//Rest 방식의 경우 데이터 자체를 전송하기 때문에 정상적인 데이터인지 비정상적인 데이터인지 확인이 필요
	//그때 이걸 사용한다.
	
	@GetMapping(value="/check",params= {"height","weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo= new SampleVO(0,""+height,""+weight);
		
		ResponseEntity<SampleVO> result = null;
		if(height<150) {
			result=ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		}else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		return result;
		
	}
	//@Pathvariable 어노테이션을 이용해서 URL 상에 경로의 일부를 파라미터로 사용가능하다.
	//Rest방식에서는 URL 자체에 데이터를 식별할 수 있는 정보들을 표현하는 경우가 많으므로 다양한 방식의 PathVariable이 사용된다.
	//@PathVariable을 적용하고 싶은 경우에는 { }을 이용해서 변수명을 지정하고 @PathVariable을 이용해서 지정된 이름의 변숫값을 얻을 수 있다.
	// 단 값을 얻을 떄에는 int double과 같은 기본자료형은 사용 할 수 없다
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(@PathVariable("cat") String cat,@PathVariable("pid") Integer pid) {
		return new String[] {"categoy:"+cat,"productid:"+pid};
	}
	
	//@RequestBody는 전달 된 (요청)(Request)의 내용(body)을 이용해서 해당 파라미터의 타입으로 반환을 요구한다.
	//내부적으로 HttpMessageConvertor 타입의 객체들을 이용해서 다양한 포맷의 입력 데이터를 변환 할 수 있다.
	//대부분은 JSON 데이터를 서버에 보내서 원하는 타입의 객체로 변환하는 용도로 사용되지만 경우에 따라서는 원하는 포맷의 데이터를 보내고 해석해서 원하는 타입으로 사용한다.
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		log.info("Convert.....ticket"+ticket);
		return ticket;
	}
}
