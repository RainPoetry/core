package com.guava.cc.bean;

/*
 * User: chenchong
 * Date: 2019/3/11
 * description:
 */

import java.util.Date;

public class Person {

	private String userName;
	private String password;
	private Date birth;
	private int age;

	public Person(String userName, String password, Date birth, int age) {
		this.userName = userName;
		this.password = password;
		this.birth = birth;
		this.age = age;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}


	@Override
	public String toString() {
		return "Person{" +
				"userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", birth=" + birth +
				", age=" + age +
				'}';
	}
}
