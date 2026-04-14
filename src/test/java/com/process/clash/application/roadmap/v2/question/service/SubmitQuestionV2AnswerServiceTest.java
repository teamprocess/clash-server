package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.SubmitQuestionV2AnswerData;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmitQuestionV2AnswerServiceTest {

    @Mock
    private QuestionV2RepositoryPort questionV2RepositoryPort;

    @Mock
    private ChapterV2RepositoryPort chapterV2RepositoryPort;

    @Mock
    private UserQuestionHistoryV2RepositoryPort userQuestionHistoryV2RepositoryPort;

    @Mock
    private UserSectionProgressRepositoryPort userSectionProgressRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    private SubmitQuestionV2AnswerService service;

    @BeforeEach
    void setUp() {
        service = new SubmitQuestionV2AnswerService(
                questionV2RepositoryPort,
                chapterV2RepositoryPort,
                userQuestionHistoryV2RepositoryPort,
                userSectionProgressRepositoryPort,
                userRepositoryPort,
                userGoodsHistoryRepositoryPort
        );
    }

    @Test
    @DisplayName("현재 진행 중인 챕터를 클리어하면 쿠키 300을 지급한다")
    void execute_grantsChapterClearCookieReward() {
        Actor actor = new Actor(1L);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 0);
        QuestionV2 question = new QuestionV2(10L, 20L, "질문", "해설", 0, 1, List.of(correctChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(question));
        ChapterV2 nextChapter = new ChapterV2(21L, 30L, "챕터 2", "설명", 1, null, List.of());
        UserSectionProgress progress = UserSectionProgress.start(actor.id(), 30L, 20L);
        User user = createUser(actor.id(), 1000);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(question));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(chapterV2RepositoryPort.findById(20L)).thenReturn(Optional.of(chapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.of(progress));
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.empty());
        when(chapterV2RepositoryPort.findAllBySectionId(30L)).thenReturn(List.of(chapter, nextChapter));
        when(userRepositoryPort.findByIdForUpdate(actor.id())).thenReturn(Optional.of(user));

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 100L)
        );

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UserGoodsHistory> historyCaptor = ArgumentCaptor.forClass(UserGoodsHistory.class);

        verify(userRepositoryPort).save(userCaptor.capture());
        verify(userGoodsHistoryRepositoryPort).save(historyCaptor.capture());

        assertThat(result.isChapterCleared()).isTrue();
        assertThat(result.nextChapterId()).isEqualTo(21L);
        assertThat(userCaptor.getValue().totalCookie()).isEqualTo(1100);
        assertThat(historyCaptor.getValue().goodsActingCategory()).isEqualTo(GoodsActingCategory.ROADMAP_V2_CHAPTER_REWARD);
        assertThat(historyCaptor.getValue().variation()).isEqualTo(100);
        assertThat(progress.getCompletedChapters()).isEqualTo(1);
        assertThat(progress.getCurrentChapterId()).isEqualTo(21L);
    }

    @Test
    @DisplayName("마지막 챕터를 클리어하면 챕터 100과 섹션 1000 쿠키를 모두 지급한다")
    void execute_grantsChapterAndSectionClearCookieRewards() {
        Actor actor = new Actor(1L);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 0);
        QuestionV2 question = new QuestionV2(10L, 20L, "질문", "해설", 0, 1, List.of(correctChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(question));
        UserSectionProgress progress = UserSectionProgress.start(actor.id(), 30L, 20L);
        User user = createUser(actor.id(), 500);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(question));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(chapterV2RepositoryPort.findById(20L)).thenReturn(Optional.of(chapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.of(progress));
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.empty());
        when(chapterV2RepositoryPort.findAllBySectionId(30L)).thenReturn(List.of(chapter));
        when(userRepositoryPort.findByIdForUpdate(actor.id())).thenReturn(Optional.of(user));

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 100L)
        );

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<UserGoodsHistory> historyCaptor = ArgumentCaptor.forClass(UserGoodsHistory.class);

        verify(userRepositoryPort).save(userCaptor.capture());
        verify(userGoodsHistoryRepositoryPort, times(2)).save(historyCaptor.capture());

        assertThat(result.isChapterCleared()).isTrue();
        assertThat(result.nextChapterId()).isNull();
        assertThat(userCaptor.getValue().totalCookie()).isEqualTo(1600);
        assertThat(historyCaptor.getAllValues())
                .extracting(UserGoodsHistory::goodsActingCategory, UserGoodsHistory::variation)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(GoodsActingCategory.ROADMAP_V2_CHAPTER_REWARD, 100),
                        org.assertj.core.groups.Tuple.tuple(GoodsActingCategory.ROADMAP_V2_SECTION_REWARD, 1000)
                );
        assertThat(progress.getIsCompleted()).isTrue();
        assertThat(progress.getCompletedChapters()).isEqualTo(1);
    }

    @Test
    @DisplayName("이미 클리어한 챕터를 다시 클리어해도 진행도와 쿠키는 증가하지 않는다")
    void execute_doesNotGrantRewardOrProgressOnReplayClear() {
        Actor actor = new Actor(1L);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 0);
        QuestionV2 question = new QuestionV2(10L, 20L, "질문", "해설", 0, 1, List.of(correctChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(question));
        ChapterV2 nextChapter = new ChapterV2(21L, 30L, "챕터 2", "설명", 1, null, List.of());
        UserSectionProgress progress = new UserSectionProgress(1L, actor.id(), 30L, 20L, 7, false);
        UserQuestionHistoryV2 clearedHistory = new UserQuestionHistoryV2(1L, actor.id(), 20L, true, 1, 1, 1);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(question));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(chapterV2RepositoryPort.findById(20L)).thenReturn(Optional.of(chapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.of(progress));
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.of(clearedHistory));

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 100L)
        );

        verify(userQuestionHistoryV2RepositoryPort).save(any(UserQuestionHistoryV2.class));
        verify(userRepositoryPort, never()).findByIdForUpdate(actor.id());
        verify(userRepositoryPort, never()).save(any(User.class));
        verify(userGoodsHistoryRepositoryPort, never()).save(any(UserGoodsHistory.class));
        verify(userSectionProgressRepositoryPort, never()).save(any(UserSectionProgress.class));
        verify(chapterV2RepositoryPort, never()).findAllBySectionId(30L);

        assertThat(result.isChapterCleared()).isTrue();
        assertThat(result.nextChapterId()).isNull();
        assertThat(progress.getCompletedChapters()).isEqualTo(7);
        assertThat(progress.getCurrentChapterId()).isEqualTo(20L);
        assertThat(clearedHistory.getCurrentQuestionIndex()).isEqualTo(1);
        assertThat(clearedHistory.getCorrectCount()).isEqualTo(1);
        assertThat(clearedHistory.isCleared()).isTrue();
    }

    @Test
    @DisplayName("진행도 레코드가 없는 첫 챕터를 최초 클리어하면 진행도가 생성되고 다음 챕터로 이동한다")
    void execute_startsProgressWhenFirstChapterClearedWithoutExistingProgress() {
        Actor actor = new Actor(1L);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 0);
        QuestionV2 question = new QuestionV2(10L, 20L, "질문", "해설", 0, 1, List.of(correctChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(question));
        ChapterV2 nextChapter = new ChapterV2(21L, 30L, "챕터 2", "설명", 1, null, List.of());
        User user = createUser(actor.id(), 100);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(question));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.empty());
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.empty());
        when(chapterV2RepositoryPort.findAllBySectionId(30L)).thenReturn(List.of(chapter, nextChapter));
        when(userRepositoryPort.findByIdForUpdate(actor.id())).thenReturn(Optional.of(user));

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 100L)
        );

        ArgumentCaptor<UserSectionProgress> progressCaptor = ArgumentCaptor.forClass(UserSectionProgress.class);

        verify(userSectionProgressRepositoryPort).save(progressCaptor.capture());
        verify(userGoodsHistoryRepositoryPort).save(any(UserGoodsHistory.class));

        UserSectionProgress savedProgress = progressCaptor.getValue();
        assertThat(result.isChapterCleared()).isTrue();
        assertThat(result.nextChapterId()).isEqualTo(21L);
        assertThat(savedProgress.getCompletedChapters()).isEqualTo(1);
        assertThat(savedProgress.getCurrentChapterId()).isEqualTo(21L);
        assertThat(savedProgress.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("오답 제출은 보상과 진행도 증가를 발생시키지 않는다")
    void execute_doesNotGrantRewardOrProgressOnWrongAnswer() {
        Actor actor = new Actor(1L);
        ChoiceV2 wrongChoice = new ChoiceV2(101L, 10L, "오답", false, 0);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 1);
        QuestionV2 question = new QuestionV2(10L, 20L, "질문", "해설", 0, 1, List.of(wrongChoice, correctChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(question));
        UserSectionProgress progress = UserSectionProgress.start(actor.id(), 30L, 20L);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(question));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(chapterV2RepositoryPort.findById(20L)).thenReturn(Optional.of(chapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.of(progress));
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.empty());

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 101L)
        );

        ArgumentCaptor<UserQuestionHistoryV2> historyCaptor = ArgumentCaptor.forClass(UserQuestionHistoryV2.class);

        verify(userQuestionHistoryV2RepositoryPort).save(historyCaptor.capture());
        verify(userRepositoryPort, never()).findByIdForUpdate(actor.id());
        verify(userRepositoryPort, never()).save(any(User.class));
        verify(userGoodsHistoryRepositoryPort, never()).save(any(UserGoodsHistory.class));
        verify(userSectionProgressRepositoryPort, never()).save(any(UserSectionProgress.class));

        UserQuestionHistoryV2 savedHistory = historyCaptor.getValue();
        assertThat(result.isCorrect()).isFalse();
        assertThat(result.isChapterCleared()).isFalse();
        assertThat(savedHistory.getCorrectCount()).isEqualTo(0);
        assertThat(savedHistory.getCurrentQuestionIndex()).isEqualTo(1);
        assertThat(savedHistory.isCleared()).isFalse();
        assertThat(progress.getCompletedChapters()).isEqualTo(0);
        assertThat(progress.getCurrentChapterId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("이미 클리어한 중간 챕터를 다시 클리어해도 현재 진행 챕터는 바뀌지 않는다")
    void execute_doesNotMoveCurrentChapterOnReplayClearOfMiddleChapter() {
        Actor actor = new Actor(1L);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 0);
        QuestionV2 question = new QuestionV2(10L, 20L, "질문", "해설", 0, 1, List.of(correctChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(question));
        ChapterV2 currentChapter = new ChapterV2(21L, 30L, "챕터 2", "설명", 1, null, List.of());
        UserSectionProgress progress = new UserSectionProgress(1L, actor.id(), 30L, 21L, 4, false);
        UserQuestionHistoryV2 clearedHistory = new UserQuestionHistoryV2(1L, actor.id(), 20L, true, 1, 1, 1);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(question));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(chapterV2RepositoryPort.findById(21L)).thenReturn(Optional.of(currentChapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.of(progress));
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.of(clearedHistory));

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 100L)
        );

        verify(userRepositoryPort, never()).findByIdForUpdate(actor.id());
        verify(userGoodsHistoryRepositoryPort, never()).save(any(UserGoodsHistory.class));
        verify(userSectionProgressRepositoryPort, never()).save(any(UserSectionProgress.class));

        assertThat(result.isChapterCleared()).isTrue();
        assertThat(progress.getCurrentChapterId()).isEqualTo(21L);
        assertThat(progress.getCompletedChapters()).isEqualTo(4);
    }

    @Test
    @DisplayName("첫 문제 재시작으로 reset이 발생해도 최초 클리어 판정은 false로 유지된다")
    void execute_keepsReplayAsNotFirstClearWhenResetOccurs() {
        Actor actor = new Actor(1L);
        ChoiceV2 correctChoice = new ChoiceV2(100L, 10L, "정답", true, 0);
        ChoiceV2 secondChoice = new ChoiceV2(200L, 11L, "정답2", true, 0);
        QuestionV2 firstQuestion = new QuestionV2(10L, 20L, "질문1", "해설1", 0, 1, List.of(correctChoice));
        QuestionV2 secondQuestion = new QuestionV2(11L, 20L, "질문2", "해설2", 1, 1, List.of(secondChoice));
        ChapterV2 chapter = new ChapterV2(20L, 30L, "챕터 1", "설명", 0, null, List.of(firstQuestion, secondQuestion));
        UserSectionProgress progress = new UserSectionProgress(1L, actor.id(), 30L, 20L, 2, true);
        UserQuestionHistoryV2 clearedHistory = new UserQuestionHistoryV2(1L, actor.id(), 20L, true, 2, 2, 2);

        when(questionV2RepositoryPort.findById(10L)).thenReturn(Optional.of(firstQuestion));
        when(chapterV2RepositoryPort.findByIdWithQuestions(20L)).thenReturn(Optional.of(chapter));
        when(chapterV2RepositoryPort.findById(20L)).thenReturn(Optional.of(chapter));
        when(userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), 30L)).thenReturn(Optional.of(progress));
        when(userQuestionHistoryV2RepositoryPort.findByUserIdAndChapterId(actor.id(), 20L)).thenReturn(Optional.of(clearedHistory));

        SubmitQuestionV2AnswerData.Result result = service.execute(
                new SubmitQuestionV2AnswerData.Command(actor, 10L, 100L)
        );

        verify(userRepositoryPort, never()).findByIdForUpdate(actor.id());
        verify(userGoodsHistoryRepositoryPort, never()).save(any(UserGoodsHistory.class));
        verify(userSectionProgressRepositoryPort, never()).save(any(UserSectionProgress.class));

        assertThat(result.isChapterCleared()).isFalse();
        assertThat(clearedHistory.getCorrectCount()).isEqualTo(1);
        assertThat(clearedHistory.getCurrentQuestionIndex()).isEqualTo(1);
        assertThat(clearedHistory.isCleared()).isFalse();
        assertThat(progress.getCompletedChapters()).isEqualTo(2);
    }

    private User createUser(Long userId, int totalCookie) {
        Instant now = Instant.now();
        return new User(
                userId,
                now,
                now,
                "user",
                "user@example.com",
                "유저",
                "password",
                Role.USER,
                "",
                0,
                totalCookie,
                Major.SERVER,
                UserStatus.ACTIVE,
                null,
                false,
                RankTier.NONE,
                ExpTier.UNRANKED
        );
    }
}
