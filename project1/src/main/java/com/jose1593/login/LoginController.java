package com.jose1593.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(HttpServletRequest request) {
		// System.out.println(request.getParameter("id"));
		// System.out.println(request.getParameter("pw"));
		LoginDTO dto = new LoginDTO();
		dto.setM_id(request.getParameter("id")); // JSP에서 보내준 id
		dto.setM_pw(request.getParameter("pw"));
		
		// 생각해주세요. id/pw를 보냈을 때 무엇이 왔으면 좋을까요?
		
		// 이름 + count(*) 값을 같이 받을 때
		dto = loginService.login(dto); 
		// Service가 일을 하고 result로 결과값 반환
		
		// System.out.println(result.getM_name());
		// System.out.println(result.getCount());
		
		if(dto.getCount() == 1) { 
			// 세션을 만들어서 로그인을 지정 시간동안 유지시킵니다. (겁나 어려움)
			HttpSession session = request.getSession(); // 외우세요 -_-;;
			session.setAttribute("mname", dto.getM_name());
			session.setAttribute("mid", request.getParameter("id")); // 내 id는 dto에 있다.
			// System.out.println(dto.getM_name());
			// System.out.println(dto.getM_id());
			
			// 세션 : 서버에 저장, 쿠키 : 클라이언트에 저장
			
			
			return "redirect:index"; // 정상적으로 로그인 했다면 인덱스로 가기 
		} else {
			return "login"; // 암호/아이디가 일치하지 않은 사람은 다시 로그인 하기
		}
		
		
	}
	
	@GetMapping("/logout") // logout mapping 해주기
	public String logout(HttpSession session) {
		if (session.getAttribute("mname") != null) { 
			// session.invalidate(); // 세션 삭제하기
			session.removeAttribute("mname"); // mname이 null이면 삭제하시오.
		}
		if (session.getAttribute("mid") != null) { 
			// session.invalidate(); // 세션 삭제하기
			session.removeAttribute("mid"); // mid가 null이면 삭제하시오.
		}
		
		session.setMaxInactiveInterval(0); // 세션 유지시간을 0으로 = 종료시키기
		
		session.invalidate(); // 세션 초기화 = 종료 = 세션의 모든 속성 값을 제거
		
		return "redirect:index";
	}
	
	@GetMapping("/join") // 7월 27일 회원가입 만들기
	public String join() {
		return "join";
	}
	
	@PostMapping("/join") 
	public String join(JoinDTO joinDTO) {
		System.out.println("jsp에서 오는 값 : " + joinDTO.getGender());
		System.out.println("jsp에서 오는 값 : " + joinDTO.getBirth());
		int result = loginService.join(joinDTO);
		
		System.out.println(result);
		if(result == 1) {
			return "redirect:/login";
		} else {
			return "redirect:/join"; // join으로 다시 돌아가버려
		}
		
		
	
	}
	// 전체 회원 뽑기
	@GetMapping("/members")
	public ModelAndView members() {
		ModelAndView mv = new ModelAndView("members");
		List<JoinDTO> list = loginService.members();
		mv.addObject("list", list);
		return mv;
	}
	
	
	
		

}

