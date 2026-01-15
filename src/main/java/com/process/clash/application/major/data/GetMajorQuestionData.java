package com.process.clash.application.major.data;

import java.util.List;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.major.entity.MajorQuestion;

import lombok.AllArgsConstructor;

public class GetMajorQuestionData {

	@AllArgsConstructor
	public static class Command {
		Actor actor;
	}

	public record Result(
			List<MajorQuestionVo> majorQuestionVos
	) {

		public static Result from(List<MajorQuestion> domainModels) {
			return new Result(
					domainModels.stream()
							.map(MajorQuestionVo::from)
							.toList()
			);
		}

		public record MajorQuestionVo(
				Long id,
				String content,
				WeightVo weightVo
		) {

			public static MajorQuestionVo from(MajorQuestion domain) {
				return new MajorQuestionVo(
						domain.getId(),
						domain.getContent(),
						WeightVo.from(domain.getWeightVo())
				);
			}

			public record WeightVo(
					Integer web,
					Integer app,
					Integer server,
					Integer ai,
					Integer game
			) {

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
