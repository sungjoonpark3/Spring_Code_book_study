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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerock.domain.SampleVO;

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
	
}
