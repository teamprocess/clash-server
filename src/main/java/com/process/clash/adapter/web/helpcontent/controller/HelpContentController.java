package com.process.clash.adapter.web.helpcontent.controller;

import com.process.clash.adapter.web.helpcontent.docs.controller.HelpContentControllerDocument;
import com.process.clash.application.helpcontent.data.GetHelpContentData;
import com.process.clash.application.helpcontent.port.in.GetHelpContentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RestController
@RequestMapping("/api/help-contents")
@RequiredArgsConstructor
public class HelpContentController implements HelpContentControllerDocument {

    private final GetHelpContentUseCase getHelpContentUseCase;

    @Override
    @GetMapping(value = "/{key}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getHelpContent(
            @PathVariable String key,
            @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch
    ) {
        GetHelpContentData.Result result = getHelpContentUseCase.execute(key);
        String etag = "\"" + result.version() + "\"";

        if (isNotModified(ifNoneMatch, etag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(etag)
                    .build();
        }

        return ResponseEntity.ok()
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .cacheControl(CacheControl.noCache())
                .eTag(etag)
                .body(result.content());
    }

    private boolean isNotModified(String ifNoneMatch, String etag) {
        if (ifNoneMatch == null) {
            return false;
        }

        return Arrays.stream(ifNoneMatch.split(","))
                .map(String::trim)
                .anyMatch(candidate -> candidate.equals("*") || candidate.equals(etag) || candidate.equals("W/" + etag));
    }
}
