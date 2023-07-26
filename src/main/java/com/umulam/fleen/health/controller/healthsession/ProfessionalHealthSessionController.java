package com.umulam.fleen.health.controller.healthsession;

import com.umulam.fleen.health.model.dto.healthsession.AddNoteHealthSessionDto;
import com.umulam.fleen.health.model.request.search.base.SearchRequest;
import com.umulam.fleen.health.model.response.FleenHealthResponse;
import com.umulam.fleen.health.model.response.healthsession.GetUpdateHealthSessionNote;
import com.umulam.fleen.health.model.security.FleenUser;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionReviewView;
import com.umulam.fleen.health.model.view.healthsession.HealthSessionView;
import com.umulam.fleen.health.model.view.search.SearchResultView;
import com.umulam.fleen.health.resolver.SearchParam;
import com.umulam.fleen.health.service.session.ProfessionalHealthSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.umulam.fleen.health.constant.base.FleenHealthConstant.HEALTH_SESSION_NOTE_UPDATED;

@Slf4j
@RestController
@RequestMapping(value = "health/professional")
public class ProfessionalHealthSessionController {

  private final ProfessionalHealthSessionService professionalHealthSessionService;

  public ProfessionalHealthSessionController(ProfessionalHealthSessionService professionalHealthSessionService) {
    this.professionalHealthSessionService = professionalHealthSessionService;
  }

  @GetMapping(value = "/sessions/entries")
  public SearchResultView viewSessions(@AuthenticationPrincipal FleenUser user, @SearchParam SearchRequest searchRequest) {
    return professionalHealthSessionService.viewSessions(user, searchRequest);
  }

  @GetMapping(value = "/session/detail/{id}")
  public HealthSessionView viewSessionDetail(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    return professionalHealthSessionService.viewSessionDetail(user, healthSessionId);
  }

  @GetMapping(value = "/session/update-note/{id}")
  public GetUpdateHealthSessionNote getUpdateHealthSessionNote(@AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    return professionalHealthSessionService.getUpdateSessionNote(user, healthSessionId);
  }

  @PutMapping(value = "/session/update-note/{id}")
  public FleenHealthResponse addNoteToSession(@Valid @RequestBody AddNoteHealthSessionDto dto, @AuthenticationPrincipal FleenUser user, @PathVariable(name = "id") Integer healthSessionId) {
    professionalHealthSessionService.addSessionNote(dto, user, healthSessionId);
    return new FleenHealthResponse(HEALTH_SESSION_NOTE_UPDATED);
  }

  @GetMapping(value = "/sessions/reviews")
  public List<HealthSessionReviewView> viewSessionReviews(@AuthenticationPrincipal FleenUser user) {
    return professionalHealthSessionService.viewReviews(user);
  }
}
