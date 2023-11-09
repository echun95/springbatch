package com.xiilab.learnwaybatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_TEST")
public class Test {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEST_ID")
	private Long id;

	@Column(name = "TEST_COUNT")
	private Long testCount;

	@Builder
	public Test(Long id, Long testCount) {
		this.id = id;
		this.testCount = testCount;
	}
	public void addCount(){
		this.testCount += 1;
	}
}
