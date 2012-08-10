package edu.utsa.tagger;

import java.util.Stack;

class Err
{
	int code;
	String message;
	
	Err(int code, String message)
	{
		this.code = code;
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
}

public final class ErrorStack
{
	private static Stack<Err> error_stack = new Stack<Err>();
	
	private ErrorStack() {}
	
	public static void push(int code, String message)
	{
		Err err = new Err(code, message);
		error_stack.push(err);
		System.out.println(message);
	}
	
	public static Err pop()
	{
		return error_stack.pop();
	}
	
	public static Err peek()
	{
		return error_stack.peek();
	}
	
	public static boolean isEmpty()
	{
		return error_stack.isEmpty();
	}
}
