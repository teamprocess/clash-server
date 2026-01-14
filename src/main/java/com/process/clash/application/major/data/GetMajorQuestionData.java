package com.process.clash.application.major.data;

import java.util.List;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.major.entity.MajorQuestion;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class GetMajorQuestionData {

	@AllArgsConstructor
	public static class Command {
		Actor actor;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Result {

		private final List<MajorQuestionVo> majorQuestionVos;

		public static Result from(List<MajorQuestion> domainModels) {
			return new Result(
					domainModels.stream()
							.map(MajorQuestionVo::from)
							.toList()
			);
		}

		@Getter
		@AllArgsConstructor(access = AccessLevel.PRIVATE)
		public static class MajorQuestionVo {
			private final Long id;
			private final String content;
			private final WeightVo weightVo;

			public static MajorQuestionVo from(MajorQuestion domain) {
				return new MajorQuestionVo(
						domain.getId(),
						domain.getContent(),
						WeightVo.from(domain.getWeightVo())
				);
			}

			@Getter
			@AllArgsConstructor(access = AccessLevel.PRIVATE)
			public static class WeightVo {
				private final Integer web;
				private final Integer app;
				private final Integer server;
				private final Integer ai;
				private final Integer game;

				public static WeightVo from(MajorQuestion.WeightVo domainWeight) {
					return new WeightVo(
							domainWeight.getWeb(),
							domainWeight.getApp(),
							domainWeight.getServer(),
							domainWeight.getAi(),
							domainWeight.getGame()
					);
				}
			}
		}
	}

}
