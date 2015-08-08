package com.booynal.maven.helloworld;

public class Helloworld {
	public static void main(String[] args) {
		System.out.println(new Helloworld().sayHello());
	}
	
	public String sayHello() {
		return "Hello Maven.";
	}
}