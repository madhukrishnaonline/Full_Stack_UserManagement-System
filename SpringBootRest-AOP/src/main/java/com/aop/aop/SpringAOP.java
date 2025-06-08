package com.aop.aop;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.aop.annotation.Crypto;
import com.aop.annotation.ExecutionTime;
import com.aop.dto.UserRequest;

@Aspect
@Component
public class SpringAOP {

	/*@Before("execution(* com.aop..*(..))")
	public void execute(JoinPoint joinPoint) {
		String name = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		System.out.println("Before method: " + name);
		System.out.println("Arguments: " + Arrays.toString(args));
	}*/

	@Around(value = "@annotation(executionTime)")
	public Object calculateTime(ProceedingJoinPoint proceedingJoinPoint, ExecutionTime executionTime) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = proceedingJoinPoint.proceed();
		long totalTimeTaken = System.currentTimeMillis() - start;
		System.out.println("Total Time Taken :: " + totalTimeTaken);
		return result;
	}

	@Around(value = "@annotation(crypto)")
	public Object validateRequestBody(ProceedingJoinPoint proceedingJoinPoint, Crypto crypto) throws Throwable {
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			List<String> values = extractStringValues(arg);
			values.forEach(val -> {
				if (val.matches(".*[<>].*")) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input :: " + val);
				}
			});
		}
		return proceedingJoinPoint.proceed();
	}

	private static List<String> extractStringValues(Object args) {
		UserRequest request = (UserRequest) args;
		List<String> list = new ArrayList<>();
		list.add(request.getUsername());
		list.add(request.getFullName());
		list.add(request.getEmail());
		list.add(request.getAddress());
		return list;
	}

	/*@After("execution(* com.aop..*(..))")
	public void execute(JoinPoint joinPoint) {
		String name = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		System.out.println("Before method: " + name);
		System.out.println("Arguments: " + Arrays.toString(args));
	}*/
}
/*private static List<String> extractStringValues(Object obj) {
	    List<String> result = new ArrayList<>();
	    if (obj == null) return result;
	
	    for (Field field : obj.getClass().getDeclaredFields()) {
	        field.setAccessible(true);
	        try {
	            Object value = field.get(obj);
	            if (value instanceof String) {
	                result.add((String) value);
	            } else if (value != null) {
	                result.addAll(extractStringValues(value)); // recursively check nested objects
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}*/